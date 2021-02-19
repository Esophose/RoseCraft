package dev.rosewood.rosecraft.world.dimension

import dev.rosewood.rosecraft.registry.Identifier
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class DimensionType(
    val name: Identifier,
    val natural: Boolean = false,
    val ambientLight: Float = 0f,
    val ceilingEnabled: Boolean = false,
    val skylightEnabled: Boolean = false,
    val fixedTime: Optional<Long> = Optional.empty(),
    val raidCapable: Boolean = false,
    val respawnAnchorSafe: Boolean = false,
    val ultrawarm: Boolean = false,
    val bedSafe: Boolean = true,
    val piglinSafe: Boolean = false,
    val logicalHeight: Int = 256,
    val coordinateScale: Int = 1,
    val infiniburn: Identifier = Identifier.of("minecraft:infiniburn_overworld")
) {

    companion object {
        private val idCounter = AtomicInteger(0)

        val OVERWORLD = DimensionType(
            Identifier.of("minecraft:overworld"),
            ultrawarm = false,
            natural = true,
            piglinSafe = false,
            respawnAnchorSafe = false,
            bedSafe = true,
            raidCapable = true,
            skylightEnabled = true,
            ceilingEnabled = false,
            ambientLight = 0f,
            logicalHeight = 256,
            infiniburn = Identifier.of("minecraft:infiniburn_overworld")
        )
    }

    private val id = idCounter.getAndIncrement()

    fun toIndexedNBT(): NBTCompound {
        val nbt = NBTCompound()
        val element = toNBT()
        nbt.setString("name", name.toString());
        nbt.setInt("id", id)
        nbt["element"] = element
        return nbt
    }

    fun toNBT(): NBTCompound {
        val nbt = NBTCompound()
            .setFloat("ambient_light", ambientLight)
            .setString("infiniburn", infiniburn.toString())
            .setByte("natural", (if (natural) 0x01 else 0x00).toByte())
            .setByte("has_ceiling", (if (ceilingEnabled) 0x01 else 0x00).toByte())
            .setByte("has_skylight", (if (skylightEnabled) 0x01 else 0x00).toByte())
            .setByte("ultrawarm", (if (ultrawarm) 0x01 else 0x00).toByte())
            .setByte("has_raids", (if (raidCapable) 0x01 else 0x00).toByte())
            .setByte("respawn_anchor_works", (if (respawnAnchorSafe) 0x01 else 0x00).toByte())
            .setByte("bed_works", (if (bedSafe) 0x01 else 0x00).toByte())
            .setByte("piglin_safe", (if (piglinSafe) 0x01 else 0x00).toByte())
            .setInt("logical_height", logicalHeight)
            .setInt("coordinate_scale", coordinateScale)
            .setString("name", name.toString())
        fixedTime.ifPresent { nbt.setLong("fixed_time", it) }
        return nbt
    }

    override fun toString(): String {
        return name.toString()
    }

}