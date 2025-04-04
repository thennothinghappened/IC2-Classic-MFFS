package mods.orca.mffs

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mods.orca.mffs.blocks.field.ProjectorTile
import mods.orca.mffs.utils.nbt.serializers.BlockPosSerializer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.storage.WorldSavedData

/**
 * Per-level manager for force field cores, projectors, and whatnot.
 */
class FieldManager(name: String) : WorldSavedData(name) {

    private var state = State()

    companion object {

        private const val DATA_NAME: String = "${MFFSMod.modId}_FieldManager"

        /**
         * Retrieve the field manager for the current level.
         */
        fun get(world: World): FieldManager {

            require(!world.isRemote) {"FieldManager should never be accessed on the client"}

            val storage = checkNotNull(world.perWorldStorage) { "world.perWorldStorage was null!!" }
            var instance = storage.getOrLoadData(FieldManager::class.java, DATA_NAME) as FieldManager?

            if (instance == null) {
                instance = FieldManager(DATA_NAME)
                storage.setData(DATA_NAME, instance)
            } else {
                instance.validateState(world)
            }

            return instance

        }

        /**
         * Retrieve the relevant field manager for a projector.
         */
        val ProjectorTile.fieldManager
            get() = get(world)

    }

    /**
     * Register a new projector in the system. On state restoration, existing projectors will be assigned their original
     * ID.
     */
    fun registerProjector(projector: ProjectorTile) {

        require(projector.id == null) {"Attempted to register projector which already knows its id"}

        // Grab the existing entry if already registered.
        // TODO: Determine if this is really slow or something. Not sure about the current setup, just getting it working first.
        for ((id, pos) in state.projectorPositions) {
            if (pos == projector.pos) {

                MFFSMod.logger.debug("FieldManager::registerProjector: Shortcutting already-registered projector {} at {}", id, pos)
                projector.id = id

                return

            }
        }

        var id = ProjectorId(0)

        while (state.projectorPositions.containsKey(id)) {
            id++
        }

        state.projectorPositions[id] = projector.pos
        markDirty()

        MFFSMod.logger.debug("FieldManager::registerProjector: Assigning ID {} to projector at {}", id, projector.pos)
        projector.id = id

    }

    /**
     * Remove a projector from the registry (i.e., it has been destroyed.)
     */
    fun deregisterProjector(projector: ProjectorTile) {

        val id = projector.id ?: return

        MFFSMod.logger.debug("FieldManager::deregisterProjector: removing projector {} at {}", id, projector.pos)
        state.projectorPositions.remove(id)
        state.fields.remove(id)
        markDirty()

    }

    /**
     * Ensure that the saved state is valid, and remove invalid entries.
     */
    private fun validateState(world: World) {
        val invalidProjectorIds = mutableSetOf<ProjectorId>()

        for ((id, pos) in state.projectorPositions) {
            if (world.getTileEntity(pos) !is ProjectorTile) {
                MFFSMod.logger.debug("Marking invalid projector {} at {}", id, pos)
                invalidProjectorIds.add(id)
            }
        }

        if (invalidProjectorIds.isNotEmpty()) {
            for (id in invalidProjectorIds) {
                state.projectorPositions.remove(id)
                state.fields.remove(id)
            }

            markDirty()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun readFromNBT(nbt: NBTTagCompound) {
        try {
            state = MFFSMod.nbt.decode(nbt)
        } catch (error: Exception) {
            MFFSMod.logger.error("Error whilst restoring FieldManager state: ", error)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {

        val serializedState = MFFSMod.nbt.encode(state) as NBTTagCompound
        compound.merge(serializedState)

        return compound

    }

    @Serializable
    private data class State(
        /**
         * Map of positions of projectors to their IDs.
         */
        val projectorPositions: MutableMap<ProjectorId, @Serializable(BlockPosSerializer::class) BlockPos> = mutableMapOf(),

        /**
         * Map of projector IDs to the blocks making up their force-field.
         */
        val fields: MutableMap<ProjectorId, MutableSet<@Serializable(BlockPosSerializer::class) BlockPos>> = mutableMapOf()
    )

}

@Serializable
@JvmInline
value class ProjectorId(private val id: Long) {
    operator fun inc(): ProjectorId {
        return ProjectorId(id + 1)
    }
}
