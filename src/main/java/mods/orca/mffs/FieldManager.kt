package mods.orca.mffs

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mods.orca.mffs.blocks.projector.TileFieldProjector
import mods.orca.mffs.utils.mutableTwoWayMapOf
import mods.orca.mffs.utils.nbt.serializers.BlockPosSerializer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.storage.WorldSavedData

/**
 * Per-level manager for force field cores, projectors, and whatnot.
 */
class WorldFieldManager(name: String) : WorldSavedData(name) {
    private val projectors = mutableTwoWayMapOf<Int, BlockPos>()
    private val fields = mutableTwoWayMapOf<Int, MutableSet<BlockPos>>()

    companion object {
        private const val DATA_NAME = "${MFFSMod.modId}_FieldManager"

        fun getOrCreate(world: World): WorldFieldManager {
            val storage = world.perWorldStorage

            return storage.getOrLoadData(WorldFieldManager::class.java, DATA_NAME)
                    as WorldFieldManager?
                ?: WorldFieldManager(DATA_NAME).also { storage.setData(DATA_NAME, it) }
        }
    }

    /**
     * Register a new projector in the system. On state restoration, existing projectors will be assigned their original
     * ID.
     */
    fun registerProjector(projector: TileFieldProjector) {
        // Grab the existing entry if already registered.
        projectors.inverse.get(projector.pos)?.let {
            MFFSMod.logger.debug("FieldManager::registerProjector: Shortcutting already-registered projector {} at {}", it, projector.pos)
            return
        }

        var id = 0

        while (projectors.containsKey(id)) {
            id++
        }

        projectors[id] = projector.pos
        markDirty()

        MFFSMod.logger.debug("FieldManager::registerProjector: Assigning ID {} to projector at {}", id, projector.pos)
    }

    /**
     * Remove a projector from the registry (i.e., it has been destroyed.)
     */
    fun deregisterProjector(projector: TileFieldProjector) {
        val id = projectors.inverse[projector.pos] ?: return

        MFFSMod.logger.debug("FieldManager::deregisterProjector: removing projector {} at {}", id, projector.pos)
        projectors.remove(id)
        fields.remove(id)
        markDirty()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun readFromNBT(nbt: NBTTagCompound) {
        try {
            val state = MFFSMod.nbt.decode<State>(nbt)

            projectors.clear()
            projectors.putAll(state.projectorPositions)

            fields.clear()
            fields.putAll(state.fields)
        } catch (error: Exception) {
            MFFSMod.logger.error("Error whilst restoring FieldManager state: ", error)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val serializedState = MFFSMod.nbt.encode(State(projectors, fields)) as NBTTagCompound
        compound.merge(serializedState)

        return compound
    }

    @Serializable
    private data class State(
        /**
         * Map of positions of projectors to their IDs.
         */
        val projectorPositions: MutableMap<
                Int,
                @Serializable(BlockPosSerializer::class) BlockPos
        >,

        /**
         * Map of projector IDs to the blocks making up their force-field.
         */
        val fields: MutableMap<
                Int,
                MutableSet<@Serializable(BlockPosSerializer::class) BlockPos>
        >
    )
}
