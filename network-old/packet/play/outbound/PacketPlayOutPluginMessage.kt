package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import dev.rosewood.rosecraft.registry.Identifier

class PacketPlayOutPluginMessage(
    private val identifier: Identifier,
    private val data: ByteArray
) : OutboundPacket(0x19) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeString(identifier.toString())
        packetData.writeBytes(data)
    }

}