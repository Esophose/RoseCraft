package dev.rosewood.rosecraft.`network-old`.packet.login.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import dev.rosewood.rosecraft.registry.Identifier

class PacketLoginOutLoginPluginRequest(
    private val messageId: Int,
    private val channel: Identifier,
    private val data: ByteArray
) : OutboundPacket(0x04) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeVarInt(messageId)
        packetData.writeString(channel.toString())
        packetData.writeBytes(data)
    }

}