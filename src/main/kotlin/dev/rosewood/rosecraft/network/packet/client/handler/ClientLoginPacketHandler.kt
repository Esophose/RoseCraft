package dev.rosewood.rosecraft.network.packet.client.handler

import dev.rosewood.rosecraft.auth.Authenticator
import dev.rosewood.rosecraft.network.PacketProcessor
import dev.rosewood.rosecraft.network.netty.cipher.CipherUtils
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.network.packet.client.login.PacketLoginInEncryptionResponse
import dev.rosewood.rosecraft.network.packet.client.login.PacketLoginInPluginResponse
import dev.rosewood.rosecraft.network.packet.client.login.PacketLoginInStart
import dev.rosewood.rosecraft.network.packet.server.login.PacketLoginOutEncryptionRequest
import dev.rosewood.rosecraft.network.packet.server.login.PacketLoginOutLoginSuccess
import dev.rosewood.rosecraft.network.player.PlayerConnection
import dev.rosewood.rosecraft.network.player.PlayerConnectionState
import dev.rosewood.rosecraft.players.PlayerList
import java.net.InetSocketAddress
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object ClientLoginPacketHandler : ClientPacketHandler() {

    init {
        register(0x00, PacketLoginInStart::class, ::startListener)
        register(0x01, PacketLoginInEncryptionResponse::class, ::encryptionResponseListener)
        register(0x02, PacketLoginInPluginResponse::class, ::pluginResponseListener)
    }

    private fun startListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        packet as PacketLoginInStart

        playerConnection.name = packet.name
        playerConnection.verifyToken = Authenticator.generateVerifyToken()

        val encryptRequestPacket = PacketLoginOutEncryptionRequest(
            playerConnection.sessionId,
            CipherUtils.generateX509Key(Authenticator.keyPair.public).encoded,
            playerConnection.verifyToken
        )
        playerConnection.sendPacket(encryptRequestPacket)
    }

    private fun encryptionResponseListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        packet as PacketLoginInEncryptionResponse

        // TODO: Disconnect on invalid encryption response
        val verifyToken: ByteArray

        val privateKey = Authenticator.keyPair.private
        val rsaCipher = Cipher.getInstance("RSA")

        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey)
        val sharedSecret = SecretKeySpec(rsaCipher.doFinal(packet.sharedSecret), "AES")

        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey)
        verifyToken = rsaCipher.doFinal(packet.verifyToken)

        if (!verifyToken.contentEquals(playerConnection.verifyToken)) {
            // TODO: Disconnect for invalid verification token
            return
        }

        // Start encryption
        PacketProcessor.clientConnections[playerConnection.channel]?.setEncryptionKey(sharedSecret)
            ?: error("ClientChannel not found")

        // Perform user authentication
        val fetchedSessionData = Authenticator.verify(
            playerConnection.name,
            playerConnection.sessionId,
            sharedSecret,
            (playerConnection.remoteAddress as InetSocketAddress).address
        )

        if (fetchedSessionData == null) {
            // TODO: Disconnect
            return
        }

        playerConnection.sessionData = fetchedSessionData

        val loginSuccessPacket = PacketLoginOutLoginSuccess(fetchedSessionData.id, fetchedSessionData.name)
        playerConnection.sendPacket(loginSuccessPacket)

        playerConnection.connectionState = PlayerConnectionState.PLAY

        PlayerList.placeNewPlayer(playerConnection)
    }

    private fun pluginResponseListener(packet: ClientPacket, playerConnection: PlayerConnection) = Unit // ignored

}
