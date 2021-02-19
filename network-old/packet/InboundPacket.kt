package dev.rosewood.rosecraft.`network-old`.packet

abstract class InboundPacket(packetId: Int, packetData: InboundPacketData) : Packet(packetId) {

    val packetLength = packetData.packetLength

}
