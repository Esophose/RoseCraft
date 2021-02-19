package dev.rosewood.rosecraft.network

import dev.rosewood.rosecraft.network.netty.channel.ClientChannel
import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.handler.ClientHandshakePacketHandler
import dev.rosewood.rosecraft.network.packet.client.handler.ClientLoginPacketHandler
import dev.rosewood.rosecraft.network.packet.client.handler.ClientPlayPacketHandler
import dev.rosewood.rosecraft.network.packet.client.handler.ClientStatusPacketHandler
import dev.rosewood.rosecraft.network.player.PlayerConnection
import dev.rosewood.rosecraft.network.player.PlayerConnectionState
import io.netty.channel.ChannelHandlerContext
import java.util.concurrent.ConcurrentHashMap

object PacketProcessor {

    val clientConnections = ConcurrentHashMap<ChannelHandlerContext, ClientChannel>()
    val playerConnections = ConcurrentHashMap<ChannelHandlerContext, PlayerConnection>()

    fun process(channel: ChannelHandlerContext, id: Int, reader: PacketReader) {
        val playerConnection = playerConnections.computeIfAbsent(channel) {
            PlayerConnection(
                channel
            )
        }

        when (playerConnection.connectionState) {
            PlayerConnectionState.HANDSHAKING -> ClientHandshakePacketHandler.handle(id, reader, playerConnection)
            PlayerConnectionState.STATUS -> ClientStatusPacketHandler.handle(id, reader, playerConnection)
            PlayerConnectionState.LOGIN -> ClientLoginPacketHandler.handle(id, reader, playerConnection)
            PlayerConnectionState.PLAY -> ClientPlayPacketHandler.handle(id, reader, playerConnection)
            PlayerConnectionState.UNKNOWN -> {
                // ???
                channel.close()
                error("Received packet during UNKNOWN player connection state")
            }
        }
    }

}
