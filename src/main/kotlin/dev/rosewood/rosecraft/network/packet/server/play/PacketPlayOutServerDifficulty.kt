package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.world.Difficulty

class PacketPlayOutServerDifficulty(
    private val difficulty: Difficulty,
    private val difficultyLocked: Boolean
) : ServerPacket(0x0D) {

    override fun write(writer: PacketWriter) {
        writer.writeByte(difficulty.id)
        writer.writeBoolean(difficultyLocked)
    }

}
