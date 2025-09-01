package mods.orca.mffs.blocks.utils

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import mods.orca.mffs.MFFSMod
import mods.orca.mffs.utils.getTagOrNull
import net.minecraft.nbt.NBTBase
import net.minecraft.tileentity.TileEntity

private const val NBT_SERIALIZED_STATE_KEY = "serializedState"

/**
 * Entity state stored as some arbitrary NBT data that may be read and written during the (de)serialization methods to
 * save and restore the block's state information.
 */
var TileEntity.serializedStateOrNull: NBTBase?
    get() = tileData.getTagOrNull(NBT_SERIALIZED_STATE_KEY)
    set(value) {
        if (value != null) {
            tileData.setTag(NBT_SERIALIZED_STATE_KEY, value)
        } else {
            tileData.removeTag(NBT_SERIALIZED_STATE_KEY)
        }
    }

/**
 * An entity which has some type-safe NBT data.
 */
interface HasNbtState<T>

inline fun <C, reified T> C.serialize(state: T)
        where C : TileEntity, C : HasNbtState<T> {
    serializedStateOrNull = MFFSMod.nbt.encode(state)
}

inline fun <C, reified T> C.deserialize(): Result<T, Throwable>?
        where C : TileEntity, C : HasNbtState<T> =
    serializedStateOrNull?.let { it ->
        runCatching { MFFSMod.nbt.decode<T>(it) }
    }
