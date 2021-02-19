package dev.rosewood.rosecraft.world.biome

import dev.rosewood.rosecraft.registry.Identifier
import org.jglrxavpok.hephaistos.nbt.NBTCompound

class BiomeEffects(
    val fogColor: Int = 0,
    val skyColor: Int = 0,
    val waterColor: Int = 0,
    val waterFogColor: Int = 0,
    val foliageColor: Int? = null,
    val grassColor: Int? = null,
    val grassColorModifier: GrassColorModifier? = null,
    //val biomeParticles: Any? = null,
    val ambientSound: Identifier? = null,
    val moodSound: MoodSound? = null,
    val additionsSound: AdditionsSound? = null,
    val music: Music? = null
) {

    fun toNbt(): NBTCompound {
        val nbt = NBTCompound()
        nbt.setInt("fog_color", fogColor)
        if (foliageColor != null) nbt.setInt("foliage_color", foliageColor)
        if (grassColor != null) nbt.setInt("grass_color", grassColor)
        nbt.setInt("sky_color", skyColor)
        nbt.setInt("water_color", waterColor)
        nbt.setInt("water_fog_color", waterFogColor)
        if (grassColorModifier != null) nbt.setString("grass_color_modifier", grassColorModifier.type)
        //if (biomeParticles != null) nbt["particle"] = biomeParticles.toNbt()
        if (ambientSound != null) nbt.setString("ambient_sound", ambientSound.toString())
        if (moodSound != null) nbt["mood_sound"] = moodSound.toNbt()
        if (additionsSound != null) nbt["additions_sound"] = additionsSound.toNbt()
        if (music != null) nbt["music"] = music.toNbt()
        return nbt
    }

    enum class GrassColorModifier(val type: String) {
        NONE("none"),
        DARK_FOREST("dark_forest"),
        SWAMP("swamp")
    }

    class MoodSound(
        val sound: Identifier,
        val tickDelay: Int = 0,
        val blockSearchExtent: Int = 0,
        val offset: Double = 0.0
    ) {
        fun toNbt(): NBTCompound {
            val nbt = NBTCompound()
            nbt.setString("sound", sound.toString())
            nbt.setInt("tick_delay", tickDelay)
            nbt.setInt("block_search_extent", blockSearchExtent)
            nbt.setDouble("offset", offset)
            return nbt
        }
    }

    class AdditionsSound(
        val sound: Identifier,
        val tickChance: Double = 0.0
    ) {
        fun toNbt(): NBTCompound {
            val nbt = NBTCompound()
            nbt.setString("sound", sound.toString())
            nbt.setDouble("tick_chance", tickChance)
            return nbt
        }
    }

    class Music(
        val sound: Identifier,
        val minDelay: Int = 0,
        val maxDelay: Int = 0,
        val replaceCurrentMusic: Boolean = false
    ) {
        fun toNbt(): NBTCompound {
            val nbt = NBTCompound()
            nbt.setString("sound", sound.toString())
            nbt.setInt("min_delay", minDelay)
            nbt.setInt("max_delay", maxDelay)
            nbt.setByte("replace_current_music", if (replaceCurrentMusic) 1.toByte() else 0.toByte())
            return nbt
        }
    }

}
