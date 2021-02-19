package dev.rosewood.rosecraft.network.packet.client.handler

import dev.rosewood.rosecraft.RoseCraft
import dev.rosewood.rosecraft.network.packet.client.ClientPacket
import dev.rosewood.rosecraft.network.packet.client.status.PacketStatusInPing
import dev.rosewood.rosecraft.network.packet.client.status.PacketStatusInRequest
import dev.rosewood.rosecraft.network.packet.server.status.PacketStatusOutPong
import dev.rosewood.rosecraft.network.packet.server.status.PacketStatusOutResponse
import dev.rosewood.rosecraft.network.player.PlayerConnection

object ClientStatusPacketHandler : ClientPacketHandler() {

    init {
        register(0x00, PacketStatusInRequest::class, ::requestListener)
        register(0x01, PacketStatusInPing::class, ::pingListener)
    }

    private fun requestListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        val responsePacket = PacketStatusOutResponse(RoseCraft.getStatus())
        playerConnection.sendPacket(responsePacket)
    }

    private fun pingListener(packet: ClientPacket, playerConnection: PlayerConnection) {
        packet as PacketStatusInPing

        val pongPacket = PacketStatusOutPong(packet.payload)
        playerConnection.sendPacket(pongPacket)

        playerConnection.close()
    }

}
