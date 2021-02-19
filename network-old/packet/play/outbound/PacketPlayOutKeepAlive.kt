package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketPlayOutKeepAlive(private val keepAliveId: Long) : OutboundPacket(0x21) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeLong(keepAliveId)
    }

}
