package dev.rosewood.rosecraft.network.packet.client.login

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket

class PacketLoginInStart : ClientPacket(0x00) {

    lateinit var name: String
        private set

    override fun read(packetReader: PacketReader) {
        name = packetReader.readString()
    }

}