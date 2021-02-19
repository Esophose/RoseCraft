package dev.rosewood.rosecraft.`network-old`.packet.login.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketLoginOutSetCompression(private val threshold: Int) : OutboundPacket(0x03) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeVarInt(threshold)
    }

}