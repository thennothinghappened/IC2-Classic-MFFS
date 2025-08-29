package mods.orca.mffs

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mods.orca.mffs.blocks.field.ForceFieldBlock
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
    private val projectorPositionById = mutableTwoWayMapOf<Int, BlockPos>()
    private val projectorPropsById = mutableMapOf<Int, ProjectorProps>()

    companion object {
        private const val DATA_NAME = "${MFFSMod.modId}_FieldManager"

        fun getOrNull(world: World): WorldFieldManager? {
            assert(!world.isRemote) { "Do not access the field manager on the client-side!!" }
            return world.perWorldStorage.getOrLoadData(WorldFieldManager::class.java, DATA_NAME) as WorldFieldManager?
        }

        fun getOrCreate(world: World): WorldFieldManager {
            return getOrNull(world) ?: WorldFieldManager(DATA_NAME).also {
                world.perWorldStorage.setData(DATA_NAME, it)
            }
        }

        fun getOrThrow(world: World): WorldFieldManager {
            return getOrNull(world) ?: error("Field manager for world $world is null")
        }
    }

    /**
     * Register a new projector in the system. On state restoration, existing projectors will be assigned their original
     * ID.
     */
    fun registerProjector(projector: TileFieldProjector) {
        // If an entry already exists, no work to do!
        projectorIdOrNull(projector)?.let {
            MFFSMod.logger.debug("FieldManager::registerProjector: Shortcutting already-registered projector {} at {}", it, projector.pos)
            return
        }

        var id = 0

        while (projectorPositionById.containsKey(id)) {
            id++
        }

        projectorPositionById[id] = projector.pos
        projectorPropsById[id] = ProjectorProps()
        markDirty()

        MFFSMod.logger.debug("FieldManager::registerProjector: Assigning ID {} to projector at {}", id, projector.pos)
    }

    /**
     * Remove a projector from the registry (i.e., it has been destroyed.)
     */
    fun deregisterProjector(projector: TileFieldProjector) {
        val id = projectorIdOrNull(projector) ?: return

        MFFSMod.logger.debug("FieldManager::deregisterProjector: removing projector {} at {}", id, projector.pos)

        assert(!isFieldEnabled(projector)) { "Projector cannot deregister when it still has a field" }

        projectorPositionById.remove(id)
        projectorPropsById.remove(id)

        markDirty()
    }

    fun enableField(projector: TileFieldProjector) {
        val id = projectorId(projector)
        val props = projectorProps(projector)
        val field = projector.getFieldShape()

        field.forEach { pos ->
            if (projectorPropsById
                .filter { it.key != id }
                .values
                .any { it.field?.contains(pos) == true }
            ) {
                return@forEach
            }

            projector.world.setBlockState(pos, ForceFieldBlock.defaultState)
        }

        props.field = field
        markDirty()
    }

    fun disableField(projector: TileFieldProjector) {
        val id = projectorId(projector)
        val props = projectorProps(projector)
        val field = props.field ?: error("Cannot disable field that is already disabled (projector $id)")

        field.forEach { pos ->
            if (projectorPropsById
                    .filter { it.key != id }
                    .values
                    .any { it.field?.contains(pos) == true }
            ) {
                return@forEach
            }

            projector.world.setBlockToAir(pos)
        }

        props.field = null
        markDirty()
    }

    fun isFieldEnabled(projector: TileFieldProjector): Boolean {
        return projectorProps(projector).field != null
    }

    private fun projectorIdOrNull(projector: TileFieldProjector): Int? {
        return projectorPositionById.inverse[projector.pos]
    }

    private fun projectorId(projector: TileFieldProjector): Int {
        return projectorIdOrNull(projector) ?: error("Cannot get ID of unregistered projector at ${projector.pos}")
    }

    private fun projectorPropsOrNull(projector: TileFieldProjector): ProjectorProps? {
        return projectorIdOrNull(projector)?.let { projectorPropsById[it] }
    }

    private fun projectorProps(projector: TileFieldProjector): ProjectorProps {
        return projectorPropsById[projectorId(projector)] ?: error("Projector at ${projector.pos} is unregistered, cannot get its props")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun readFromNBT(nbt: NBTTagCompound) {
        try {
            val state = MFFSMod.nbt.decode<State>(nbt)

            projectorPositionById.clear()
            projectorPropsById.clear()

            state.projectors.forEach { (id, savedProjectorProps) ->
                projectorPositionById[id] = savedProjectorProps.pos
                projectorPropsById[id] = savedProjectorProps.props
            }
        } catch (error: Exception) {
            MFFSMod.logger.error("Error whilst restoring FieldManager state: ", error)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val state = State(
            projectors = projectorPositionById.mapValues { (id, pos) ->
                State.SavedProjectorProps(
                    pos = pos,
                    props = projectorPropsById[id]!!
                )
            }
        )

        val serializedState = MFFSMod.nbt.encode(state) as NBTTagCompound
        compound.merge(serializedState)

        return compound
    }

    @Serializable
    private data class State(
        val projectors: Map<Int, SavedProjectorProps>
    ) {
        @Serializable
        data class SavedProjectorProps(
            val pos: @Serializable(BlockPosSerializer::class) BlockPos,
            val props: ProjectorProps
        )
    }

    @Serializable
    private data class ProjectorProps(
        var field: Set<@Serializable(BlockPosSerializer::class) BlockPos>? = null
    )
}
