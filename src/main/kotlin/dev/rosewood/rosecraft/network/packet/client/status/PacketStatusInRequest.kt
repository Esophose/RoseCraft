package dev.rosewood.rosecraft.network.packet.client.status

import dev.rosewood.rosecraft.network.packet.PacketReader
import dev.rosewood.rosecraft.network.packet.client.ClientPacket

class PacketStatusInRequest : ClientPacket(0x00) {

    override fun read(packetReader: PacketReader) = Unit

}