package dev.rosewood.rosecraft.`network-old`.packet.status.inbound

import dev.rosewood.rosecraft.`network-old`.packet.InboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacketData

class PacketStatusInPing(packetData: InboundPacketData) : InboundPacket(0x01, packetData) {

    val payload = packetData.readLong()

}
