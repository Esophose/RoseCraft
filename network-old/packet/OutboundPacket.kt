package dev.rosewood.rosecraft.`network-old`.packet

abstract class OutboundPacket(packetId: Int) : Packet(packetId) {

    abstract fun write(packetData: OutboundPacketData)

}