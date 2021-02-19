package dev.rosewood.rosecraft.network.packet.client

import dev.rosewood.rosecraft.network.packet.Packet
import dev.rosewood.rosecraft.network.packet.PacketReader

abstract class ClientPacket(id: Int) : Packet(id) {

    abstract fun read(packetReader: PacketReader)

}
