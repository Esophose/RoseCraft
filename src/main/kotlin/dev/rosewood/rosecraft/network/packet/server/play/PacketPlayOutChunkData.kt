package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket

class PacketPlayOutChunkData : ServerPacket(0x20) {

    override fun write(writer: PacketWriter) {
        writer.writeInt(0) // chunk x
        writer.writeInt(0) // chunk z
        writer.writeBoolean(true) // full chunk
        writer.writeVarInt(0) // primary bit mask
    }

}
