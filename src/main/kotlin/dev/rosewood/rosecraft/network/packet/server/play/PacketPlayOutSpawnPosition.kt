package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.world.BlockPosition

class PacketPlayOutSpawnPosition(private val location: BlockPosition) : ServerPacket(0x42) {

    override fun write(writer: PacketWriter) {
        writer.writePosition(location)
    }

}
