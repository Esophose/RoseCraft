package dev.rosewood.rosecraft.`network-old`.packet.login.inbound

import dev.rosewood.rosecraft.`network-old`.packet.InboundPacket
import dev.rosewood.rosecraft.`network-old`.packet.InboundPacketData

class PacketLoginInEncryptionResponse(packetData: InboundPacketData) : InboundPacket(0x01, packetData) {

    private val sharedSecretLength = packetData.readVarInt()
    val sharedSecret = packetData.readBytes(sharedSecretLength)
    private val verifyTokenLength = packetData.readVarInt()
    val verifyToken = packetData.readBytes(verifyTokenLength)

}