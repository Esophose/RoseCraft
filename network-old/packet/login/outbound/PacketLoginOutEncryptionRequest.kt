package dev.rosewood.rosecraft.`network-old`.packet.login.outbound

import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.OutboundPacketData

class PacketLoginOutEncryptionRequest(
    private val serverId: String,
    private val publicKey: ByteArray,
    private val verifyToken: ByteArray
) : OutboundPacket(0x01) {

    override fun write(packetData: OutboundPacketData) {
        packetData.writeString(serverId)
        packetData.writeVarInt(publicKey.size)
        packetData.writeBytes(publicKey)
        packetData.writeVarInt(verifyToken.size)
        packetData.writeBytes(verifyToken)
    }

}
