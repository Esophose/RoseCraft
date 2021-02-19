package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import dev.rosewood.rosecraft.world.BlockPosition

class PacketPlayOutSpawnPosition(private val location: BlockPosition) : OutboundPacket(0x4E) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writePosition(location)
    }

}
