package dev.rosewood.rosecraft.`network-old`.packet.status.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketStatusOutPong(private val payload: Long) : OutboundPacket(0x01) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeLong(payload)
    }

}
