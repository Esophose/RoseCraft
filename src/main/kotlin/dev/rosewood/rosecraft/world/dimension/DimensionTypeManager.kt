package dev.rosewood.rosecraft.world.dimension

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import java.util.*

object DimensionTypeManager {

    private val dimensionTypes: MutableList<DimensionType> = LinkedList()

    init {
        addDimension(DimensionType.OVERWORLD)
    }

    /**
     * Add a new dimension type. This does NOT send the new list to players.
     * @param dimensionType
     */
    fun addDimension(dimensionType: DimensionType) {
        dimensionTypes.add(dimensionType)
    }

    /**
     * Removes a dimension type. This does NOT send the new list to players.
     * @param dimensionType
     * @return if the dimension type was removed, false if it was not present before
     */
    fun removeDimension(dimensionType: DimensionType): Boolean {
        return dimensionTypes.remove(dimensionType)
    }

    /**
     * Returns an immutable copy of the dimension types already registered
     * @return
     */
    fun unmodifiableList(): List<DimensionType> {
        return Collections.unmodifiableList(dimensionTypes)
    }

    fun toNBT(): NBTCompound {
        val dimensions = NBTCompound()
        dimensions.setString("type", "minecraft:dimension_type")
        val dimensionList = NBTList<NBTCompound>(NBTTypes.TAG_Compound)
        for (dimensionType in dimensionTypes)
            dimensionList.add(dimensionType.toIndexedNBT())
        dimensions["value"] = dimensionList
        return dimensions
    }

}
