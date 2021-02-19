package dev.rosewood.rosecraft.`network-old`.packet.login.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData
import java.util.UUID

class PacketLoginOutLoginSuccess(private val uuid: UUID, private val username: String) : OutboundPacket(0x02) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeString(uuid.toString())
        packetData.writeString(username)
    }

}
