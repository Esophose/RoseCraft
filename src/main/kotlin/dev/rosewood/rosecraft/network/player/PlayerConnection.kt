package dev.rosewood.rosecraft.network.player

import dev.rosewood.rosecraft.auth.Authenticator
import dev.rosewood.rosecraft.auth.SessionData
import dev.rosewood.rosecraft.entity.player.Player
import dev.rosewood.rosecraft.network.packet.PacketUtils
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.network.packet.server.login.PacketLoginOutDisconnect
import dev.rosewood.rosecraft.network.packet.server.play.PacketPlayOutDisconnect
import dev.rosewood.rosecraft.players.PlayerList
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import java.net.SocketAddress

class PlayerConnection(val channel: ChannelHandlerContext) {

    var connectionState = PlayerConnectionState.HANDSHAKING
    var online = true
    var protocolVersion = -1
    lateinit var sessionData: SessionData

    //region Authentication variables
    val sessionId = Authenticator.generateSessionId()
    lateinit var name: String
    lateinit var verifyToken: ByteArray
    //endregion

    val remoteAddress: SocketAddress
        get() = channel.channel().remoteAddress()

    val player: Player
        get() {
            if (connectionState != PlayerConnectionState.PLAY)
                error("Can only get Player during PLAY state")

            return PlayerList.playersByUUID[sessionData.id]!!
        }

    fun sendPacket(buffer: ByteBuf) {
        buffer.retain()
        channel.writeAndFlush(buffer)
    }

    fun sendPacket(packet: ServerPacket) {
        val buffer = PacketUtils.writePacket(packet)
        sendPacket(buffer)
    }

    fun writePacket(buffer: ByteBuf) {
        buffer.retain()
        channel.write(buffer)
    }

    fun flush() {
        channel.flush()
    }

    fun close() {
        channel.close()
    }

    fun disconnect(reason: String = "An unknown error occurred") {
        val disconnectPacket = when (connectionState) {
            PlayerConnectionState.LOGIN -> PacketLoginOutDisconnect(reason)
            PlayerConnectionState.PLAY -> PacketPlayOutDisconnect(reason)
            else -> null
        }

        if (disconnectPacket != null)
            sendPacket(disconnectPacket)

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

}
