package dev.rosewood.rosecraft.`network-old`.packet.login.inbound

import dev.rosewood.rosecraft.`network-old`.packet.InboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacketData

class PacketLoginInStart(packetData: InboundPacketData) : InboundPacket(0x00, packetData) {

    val name = packetData.readString()

}