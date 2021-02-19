package dev.rosewood.rosecraft.`network-old`

import dev.rosewood.rosecraft.RoseCraft
import dev.rosewood.rosecraft.auth.Authenticator
import dev.rosewood.rosecraft.auth.SessionData
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacketData
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import dev.rosewood.rosecraft.`network-old`.packet.handshake.inbound.PacketHandshakeInRequest
import dev.rosewood.rosecraft.`network-old`.packet.login.inbound.PacketLoginInEncryptionResponse
import dev.rosewood.rosecraft.`network-old`.packet.login.inbound.PacketLoginInStart
import dev.rosewood.rosecraft.`network-old`.packet.login.outbound.PacketLoginOutDisconnect
import dev.rosewood.rosecraft.`network-old`.packet.login.outbound.PacketLoginOutEncryptionRequest
import dev.rosewood.rosecraft.`network-old`.packet.login.outbound.PacketLoginOutLoginSuccess
import dev.rosewood.rosecraft.`network-old`.packet.play.outbound.PacketPlayOutDisconnect
import dev.rosewood.rosecraft.`network-old`.packet.status.inbound.PacketStatusInPing
import dev.rosewood.rosecraft.`network-old`.packet.status.inbound.PacketStatusInRequest
import dev.rosewood.rosecraft.`network-old`.packet.status.outbound.PacketStatusOutPong
import dev.rosewood.rosecraft.`network-old`.packet.status.outbound.PacketStatusOutResponse
import dev.rosewood.rosecraft.network.netty.cipher.CipherUtils
import dev.rosewood.rosecraft.network.packet.PacketUtils
import dev.rosewood.rosecraft.players.PlayerList
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class PlayerConnection(private val serverSocket: Socket, val playerId: Int) :
    Thread("PlayerConnection #$playerId"), AutoCloseable {

    private val inStream = DataInputStream(serverSocket.getInputStream())
    private val outStream = DataOutputStream(serverSocket.getOutputStream())
    var state = PlayerConnectionState.HANDSHAKING
    private var encrypted = false

    private val sessionId = Authenticator.generateSessionId()
    private var clientProtocol: Int = -1
    lateinit var sharedSecret: SecretKeySpec
    lateinit var sessionData: SessionData

    var keepAliveFailures = 0

    override fun run() = handleHandshake()

    private fun handleHandshake() {
        state = PlayerConnectionState.HANDSHAKING

        val packetData = InboundPacketData(inStream)
        val handshakePacket = PacketHandshakeInRequest(packetData)

        clientProtocol = handshakePacket.protocolVersion

        when (handshakePacket.nextState) {
            PlayerConnectionState.STATUS -> handleStatus()
            PlayerConnectionState.LOGIN -> handleLogin()
            else -> {
            } // invalid, do nothing
        }
    }

    private fun handleStatus() {
        state = PlayerConnectionState.STATUS

        val requestData = InboundPacketData(inStream)
        PacketStatusInRequest(requestData) // Completely unused

        val responsePacket = PacketStatusOutResponse(RoseCraft.getStatus())
        sendPacket(responsePacket)

        val pingData = InboundPacketData(inStream)
        val payload = PacketStatusInPing(pingData).payload

        val pongPacket = PacketStatusOutPong(payload)
        sendPacket(pongPacket)
        close()
    }

    private fun handleLogin() {
        state = PlayerConnectionState.LOGIN

        val startData = InboundPacketData(inStream)
        val name = PacketLoginInStart(startData).name

        println("Logging in $name [${serverSocket.inetAddress}]")

        if (clientProtocol != RoseCraft.protocol) {
            disconnect("Protocol mismatch. Required: ${RoseCraft.protocol} Found: $clientProtocol")
            return
        }

        val randomToken = Authenticator.generateVerifyToken()
        val encryptionRequestPacket = PacketLoginOutEncryptionRequest(
            sessionId,
            CipherUtils.generateX509Key(Authenticator.keyPair.public).encoded,
            randomToken
        )
        sendPacket(encryptionRequestPacket)

        val responseData = InboundPacketData(inStream)
        val encryptionResponsePacket = PacketLoginInEncryptionResponse(responseData)

        val verifyToken: ByteArray
        try {
            val privateKey = Authenticator.keyPair.private
            val rsaCipher = Cipher.getInstance("RSA")

            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey)
            sharedSecret = SecretKeySpec(rsaCipher.doFinal(encryptionResponsePacket.sharedSecret), "AES")

            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey)
            verifyToken = rsaCipher.doFinal(encryptionResponsePacket.verifyToken)
        } catch (e: Exception) {
            var message = e.message
            if (message == null)
                message = "Error decrypting encryption response"
            disconnect(message)
            return
        }

        if (!verifyToken.contentEquals(randomToken)) {
            disconnect("Invalid verification token")
            return
        }

        val fetchedSessionData = Authenticator.verify(name, sessionId, sharedSecret, serverSocket.inetAddress)
        if (fetchedSessionData == null) {
            disconnect("Error authenticating session")
            return
        }

        sessionData = fetchedSessionData

        println("Authenticated ${sessionData.name} [${sessionData.id}]")

        encrypted = true

        val packetLoginSuccess = PacketLoginOutLoginSuccess(sessionData.id, sessionData.name)
        sendPacket(packetLoginSuccess)

        handlePlay()
    }

    private fun handlePlay() {
        PlayerList.placeNewPlayer(this, sessionData)

        while (true) {
            try {
                val cipher: Cipher?
                if (encrypted) {
                    cipher = Cipher.getInstance("AES/CFB8/NoPadding")
                    cipher.init(Cipher.DECRYPT_MODE, sharedSecret, IvParameterSpec(sharedSecret.encoded))
                } else {
                    cipher = null
                }

                // TODO: PacketRegistry
                val packetData = InboundPacketData(inStream, cipher)
                packetData.readBytes(packetData.available()) // burn the rest of the data, we can't handle anything yet

                keepAliveFailures = 0
            } catch (e: Exception) {
                close(e.message)
                break
            }
        }

        // Connection ended
        close(null)
    }

    fun sendPacket(packet: OutboundPacket) {
        val cipher: Cipher?
        if (encrypted) {
            cipher = Cipher.getInstance("AES/CFB8/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, sharedSecret, IvParameterSpec(sharedSecret.encoded))
        } else {
            cipher = null
        }

        val packetData = OutboundPacketData()
        packetData.writeVarInt(packet.packetId)
        packet.write(packetData)
        println(packetData.byteArrayOutputStream.size() + PacketUtils.lengthVarInt(packetData.byteArrayOutputStream.size()))
        packetData.apply(outStream, cipher)
        outStream.flush()
    }

    fun disconnect(reason: String = "An unknown error occurred") {
        if (state == PlayerConnectionState.LOGIN) {
            val disconnectPacket = PacketLoginOutDisconnect(reason)
            sendPacket(disconnectPacket)
        } else if (state == PlayerConnectionState.PLAY) {
            val disconnectPacket = PacketPlayOutDisconnect(reason)
            sendPacket(disconnectPacket)
        }

        close(reason)
    }

    fun close(message: String?) {
        if (message != null) {
            println("Connection was forcibly closed by remote host: $message")
        } else {
            println("Connection was forcibly closed by remote host")
        }
        close()
    }

    override fun close() {
        inStream.close()
        outStream.close()
        serverSocket.close()
        state = PlayerConnectionState.CLOSED
        PlayerList.remove(this)
    }

}
