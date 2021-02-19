package dev.rosewood.rosecraft.world.biome

import dev.rosewood.rosecraft.registry.Identifier
import org.jglrxavpok.hephaistos.nbt.NBTCompound

import java.util.concurrent.atomic.AtomicInteger

class Biome(
    val name: Identifier,
    val depth: Float = 0.2f,
    val temperature: Float = 0.25f,
    val scale: Float = 0.2f,
    val downfall: Float = 0.8f,
    val category: Category = Category.NONE,
    val effects: BiomeEffects,
    val precipitation: Precipitation = Precipitation.RAIN,
    val temperatureModifier: TemperatureModifier = TemperatureModifier.NONE
) {

    companion object {
        private val idCounter = AtomicInteger(0)

        // A plains biome has to be registered or else minecraft will crash
        val PLAINS: Biome = Biome(
            name = Identifier.of("minecraft:plains"),
            category = Category.NONE,
            temperature = 0.8f,
            downfall = 0.4f,
            depth = 0.125f,
            scale = 0.05f,
            effects = BiomeEffects(
                fogColor = 0xC0D8FF,
                skyColor = 0x78A7FF,
                waterColor = 0x3F76E4,
                waterFogColor = 0x50533
            )
        )
    }

    val id = idCounter.getAndIncrement()

    fun toNbt(): NBTCompound {
        val nbt = NBTCompound()
        nbt.setString("name", name.toString())
        nbt.setInt("id", id)
        val element = NBTCompound()
        element.setFloat("depth", depth)
        element.setFloat("temperature", temperature)
        element.setFloat("scale", scale)
        element.setFloat("downfall", downfall)
        element.setString("category", category.type)
        element.setString("precipitation", precipitation.type)
        if (temperatureModifier != TemperatureModifier.NONE) element.setString("temperature_modifier", temperatureModifier.type)
        element["effects"] = effects.toNbt()
        nbt["element"] = element
        return nbt
    }

    enum class Precipitation(val type: String) {
        RAIN("rain"),
        NONE("none"),
        SNOW("snow")
    }

    enum class Category(val type: String) {
        NONE("none"),
        TAIGA("taiga"),
        EXTREME_HILLS("extreme_hills"),
        JUNGLE("jungle"),
        MESA("mesa"),
        PLAINS("plains"),
        SAVANNA("savanna"),
        ICY("icy"),
        THE_END("the_end"),
        BEACH("beach"),
        FOREST("forest"),
        OCEAN("ocean"),
        DESERT("desert"),
        RIVER("river"),
        SWAMP("swamp"),
        MUSHROOM("mushroom"),
        NETHER("nether")
    }

    enum class TemperatureModifier(val type: String) {
        NONE("none"),
        FROZEN("frozen")
    }

}