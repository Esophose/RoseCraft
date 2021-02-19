package dev.rosewood.rosecraft.`network-old`.packet.status.inbound

import dev.rosewood.rosecraft.`network-old`.packet.InboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacketData

class PacketStatusInRequest(packetData: InboundPacketData) : InboundPacket(0x00, packetData)
