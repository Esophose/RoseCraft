package dev.rosewood.rosecraft.`network-old`.packet.status.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketStatusOutResponse(private val status: String) : OutboundPacket(0x00) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeString(status)
    }

}
