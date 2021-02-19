package dev.rosewood.rosecraft.network.packet.server.play

import dev.rosewood.rosecraft.network.packet.PacketWriter
import dev.rosewood.rosecraft.network.packet.server.ServerPacket
import dev.rosewood.rosecraft.players.PlayerList
import dev.rosewood.rosecraft.registry.Identifier
import dev.rosewood.rosecraft.world.biome.BiomeManager
import dev.rosewood.rosecraft.world.dimension.DimensionType
import dev.rosewood.rosecraft.world.dimension.DimensionTypeManager
import org.jglrxavpok.hephaistos.nbt.NBTCompound

class PacketPlayOutJoinGame(private val entityId: Int) : ServerPacket(0x24) {

    override fun write(writer: PacketWriter) {
        writer.writeInt(entityId) // entity id
        writer.writeBoolean(false) // is hardcore
        writer.writeByte(0b000) // gamemode - survival
        writer.writeByte(0b000) // previous gamemode - survival
        writer.writeVarInt(1) // world count
        writer.writeString("minecraft:world") // world names (array)

        val nbt = NBTCompound()
        val dimensions = DimensionTypeManager.toNBT()
        val biomes = BiomeManager.toNBT()

        nbt["minecraft:dimension_type"] = dimensions
        nbt["minecraft:worldgen/biome"] = biomes

        writer.writeNBT("", nbt) // dimensions/biomes NBT
        writer.writeNBT("", DimensionType.OVERWORLD.toNBT()) // dimension being spawned into NBT

        writer.writeString(DimensionType.OVERWORLD.name.toString()) // world name (being spawned into)
        writer.writeLong(0) // seed
        writer.writeVarInt(PlayerList.maxPlayers) // max players
        writer.writeVarInt(12) // render distance
        writer.writeBoolean(false) // reduced debug
        writer.writeBoolean(true) // enable respawn screen
        writer.writeBoolean(false) // is debug world
        writer.writeBoolean(false) // is superflat world
    }

}
