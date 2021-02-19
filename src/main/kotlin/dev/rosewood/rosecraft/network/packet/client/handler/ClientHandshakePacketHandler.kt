package dev.rosewood.rosecraft.network.packet.client.handler

import dev.rosewood.rosecraft.RoseCraft
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.network.packet.client.handshake.PacketHandshakeInRequest
import dev.rosewood.rosecraft.network.packet.server.login.PacketLoginOutDisconnect
import dev.rosewood.rosecraft.network.player.PlayerConnection
import dev.rosewood.rosecraft.network.player.PlayerConnectionState

object ClientHandshakePacketHandler : ClientPacketHandler() {

    init {
        register(0x00, PacketHandshakeInRequest::class, ::handshakeListener)
    }

    private fun handshakeListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        packet as PacketHandshakeInRequest

        playerConnection.connectionState = packet.nextState
        playerConnection.protocolVersion = packet.protocolVersion

        if (packet.nextState == PlayerConnectionState.LOGIN) {
            when {
                playerConnection.protocolVersion > RoseCraft.protocol -> {
                    val disconnectPacket = PacketLoginOutDisconnect("Outdated server! I'm still on ${RoseCraft.gameVersion}!")
                    playerConnection.sendPacket(disconnectPacket)
                    playerConnection.close()
                }
                playerConnection.protocolVersion < RoseCraft.protocol -> {
                    val disconnectPacket = PacketLoginOutDisconnect("Outdated client! I'm on ${RoseCraft.gameVersion}!")
                    playerConnection.sendPacket(disconnectPacket)
                    playerConnection.close()
                }
            }
        }
    }

}
