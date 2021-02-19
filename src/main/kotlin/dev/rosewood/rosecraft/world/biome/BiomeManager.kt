package dev.rosewood.rosecraft.world.biome

import dev.rosewood.rosecraft.registry.Identifier
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import java.util.*


object BiomeManager {

    private val biomes: Int2ObjectMap<Biome> = Int2ObjectOpenHashMap()

    init {
        addBiome(Biome.PLAINS)
    }

    /**
     * Add a new biome. This does NOT send the new list to players.
     *
     * @param biome the biome to add
     */
    fun addBiome(biome: Biome) {
        biomes[biome.id] = biome
    }

    /**
     * Removes a biome. This does NOT send the new list to players.
     *
     * @param biome the biome to remove
     */
    fun removeBiome(biome: Biome) {
        biomes.remove(biome.id)
    }

    /**
     * Returns an immutable copy of the biomes already registered
     *
     * @return an immutable copy of the biomes already registered
     */
    fun unmodifiableCollection(): Collection<Biome> {
        return Collections.unmodifiableCollection(biomes.values)
    }

    /**
     * Get a biome by its id
     *
     * @param id the id of the biome
     * @return the [Biome] linked to this id
     */
    fun getById(id: Int): Biome? {
        return biomes.get(id)
    }

    fun getByName(identifier: Identifier): Biome? {
        for (biome in biomes.values)
            if (biome.name == identifier)
                return biome
        return null
    }

    fun toNBT(): NBTCompound {
        val biomes = NBTCompound()
        biomes.setString("type", "minecraft:worldgen/biome")
        val biomesList = NBTList<NBTCompound>(NBTTypes.TAG_Compound)
        for (biome in BiomeManager.biomes.values)
            biomesList.add(biome.toNbt())
        biomes["value"] = biomesList
        return biomes
    }

}
