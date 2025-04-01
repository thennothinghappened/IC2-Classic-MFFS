package mods.orca.mffs.utils

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos

/**
 * Ordered list of NBT tag types whose ordinal values correspond to their tag ID for type checking.
 */
enum class NBTTagType {
    END,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BYTE_ARRAY,
    STRING,
    LIST,
    COMPOUND,
    INT_ARRAY,
    LONG_ARRAY
}

/**
 * Check whether this compound contains the given key of the expected type.
 *
 * @param key The name of the key to check.
 * @param type The type of tag expected.
 */
fun NBTTagCompound.hasKey(key: String, type: NBTTagType) =
    hasKey(key, type.ordinal)

/**
 * Get a [Double] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getDoubleOrNull(key: String) =
    if (hasKey(key, NBTTagType.DOUBLE))
        getDouble(key)
    else null

/**
 * Get a [Boolean] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getBooleanOrNull(key: String) =
    if (hasKey(key, NBTTagType.BYTE))
        getBoolean(key)
    else null

fun List<Int>.toBlockPos() = BlockPos(get(0), get(1), get(2))
fun IntArray.toBlockPos() = BlockPos(get(0), get(1), get(2))
fun BlockPos.toIntArray() = intArrayOf(x, y, z)
fun BlockPos.toList() = listOf(x, y, z)
