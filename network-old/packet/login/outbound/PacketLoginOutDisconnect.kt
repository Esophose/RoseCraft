package dev.rosewood.rosecraft.`network-old`.packet.login.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketLoginOutDisconnect(private val reason: String) : OutboundPacket(0x00) {

    override fun write(packetData: OutboundPacketData) {
        val json = """
            {
                "text": "$reason"
            }
        """.trimIndent()
        packetData.writeString(json)
    }

}