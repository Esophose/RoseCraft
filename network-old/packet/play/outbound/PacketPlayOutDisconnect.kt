package dev.rosewood.rosecraft.`network-old`.packet.play.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketPlayOutDisconnect(private val reason: String) : OutboundPacket(0x1B) {

    override fun write(packetData: OutboundPacketData) {
        val json = """
            {
                "text": "$reason"
            }
        """.trimIndent()
        packetData.writeString(json)
    }

}
