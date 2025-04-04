package mods.orca.mffs.utils

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.math.BlockPos

/**
 * Ordered list of NBT tag types whose ordinal values correspond to their tag ID for type checking.
 */
enum class NbtType {
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
private fun NBTTagCompound.hasKey(key: String, type: NbtType) =
    hasKey(key, type.ordinal)

/**
 * Gets a tag of any type from this structure, or returns null if the tag is not present.
 */
fun NBTTagCompound.getTagOrNull(key: String) =
    if (hasKey(key)) getTag(key) else null

/**
 * Get a [NBTTagList] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getListOrNull(key: String, listType: NbtType) =
    if (hasKey(key, NbtType.LIST)) getTagList(key, listType.ordinal) else null

/**
 * Get a [NBTTagCompound] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getCompoundOrNull(key: String) =
    if (hasKey(key, NbtType.COMPOUND)) getCompoundTag(key) else null

/**
 * Get a [String] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getStringOrNull(key: String) =
    if (hasKey(key, NbtType.STRING)) getString(key) else null

/**
 * Get a [Int] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getIntOrNull(key: String) =
    if (hasKey(key, NbtType.INT)) getInteger(key) else null

/**
 * Get a [Double] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getDoubleOrNull(key: String) =
    if (hasKey(key, NbtType.DOUBLE)) getDouble(key) else null

/**
 * Get a [Boolean] value from this compound, or return null if it is invalid or of the incorrect type.
 */
fun NBTTagCompound.getBooleanOrNull(key: String) =
    if (hasKey(key, NbtType.BYTE)) getBoolean(key) else null

fun List<Int>.toBlockPos() = BlockPos(get(0), get(1), get(2))
fun IntArray.toBlockPos() = BlockPos(get(0), get(1), get(2))
fun BlockPos.toIntArray() = intArrayOf(x, y, z)
fun BlockPos.toList() = listOf(x, y, z)
