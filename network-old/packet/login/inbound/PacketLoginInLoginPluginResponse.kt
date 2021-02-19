package dev.rosewood.rosecraft.`network-old`.packet.login.inbound

import dev.rosewood.rosecraft.`network-old`.packet.InboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacketData

class PacketLoginInLoginPluginResponse(packetData: InboundPacketData) : InboundPacket(0x02, packetData) {

    val messageId = packetData.readVarInt()
    val successful = packetData.readBoolean()
    val data: ByteArray?

    init {
        data = if (successful) {
            val dataSize = packetData.available()
            packetData.readBytes(dataSize)
        } else null
    }

}