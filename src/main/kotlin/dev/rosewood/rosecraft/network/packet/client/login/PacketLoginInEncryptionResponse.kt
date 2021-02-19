package dev.rosewood.rosecraft.network.packet.client.login

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket

class PacketLoginInEncryptionResponse : ClientPacket(0x01) {

    lateinit var sharedSecret: ByteArray
        private set
    lateinit var verifyToken: ByteArray
        private set

    override fun read(packetReader: PacketReader) {
        val sharedSecretLength = packetReader.readVarInt()
        sharedSecret = packetReader.readBytes(sharedSecretLength)
        val verifyTokenLength = packetReader.readVarInt()
        verifyToken = packetReader.readBytes(verifyTokenLength)
    }

}