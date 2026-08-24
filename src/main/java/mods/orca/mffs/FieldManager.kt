package mods.orca.mffs

import kotlinx.serialization.Serializable
import mods.orca.mffs.blocks.core.ForceFieldCoreTile
import mods.orca.mffs.blocks.projector.FieldProjectorTile
import mods.orca.mffs.registry.Registry
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

    private val cores = mutableTwoWayMapOf<CoreId, BlockPos>()
    private var nextCoreId: CoreId = CoreId(0)

    companion object {
        private const val DATA_NAME = "${MFFSMod.modId}_FieldManager"

        fun getOrNull(world: World): WorldFieldManager? {
            assert(!world.isRemote) { "Do not access the field manager on the client-side!!" }
            return world.perWorldStorage.getOrLoadData(WorldFieldManager::class.java, DATA_NAME) as WorldFieldManager?
        }

        fun get(world: World): WorldFieldManager {
            return getOrNull(world) ?: WorldFieldManager(DATA_NAME).also {
                world.perWorldStorage.setData(DATA_NAME, it)
            }
        }
    }

    /**
     * Register a new projector in the system. On state restoration, existing projectors will be assigned their original
     * ID.
     */
    fun registerProjector(projector: FieldProjectorTile) {
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
    fun deregisterProjector(projector: FieldProjectorTile) {
        val id = projectorIdOrNull(projector) ?: return

        MFFSMod.logger.debug("FieldManager::deregisterProjector: removing projector {} at {}", id, projector.pos)

        assert(!isFieldEnabled(projector)) { "Projector cannot deregister when it still has a field" }

        projectorPositionById.remove(id)
        projectorPropsById.remove(id)

        markDirty()
    }

    fun enableField(projector: FieldProjectorTile) {
        val id = projectorId(projector)
        val props = projectorProps(projector)

        val field = projector.getFieldShape()
        props.field = field

        field.forEach { pos ->
            if (getOwningProjector(pos) == id) {
                projector.world.setBlockState(pos, Registry.Blocks.forceField.defaultState)
            }
        }

        markDirty()
    }

    fun disableField(projector: FieldProjectorTile) {
        val id = projectorId(projector)
        val props = projectorProps(projector)

        val field = props.field ?: error("Cannot disable field that is already disabled (projector $id)")
        props.field = null

        field.forEach { pos ->
            if (getOwningProjector(pos) == null) {
                projector.world.setBlockToAir(pos)
            }
        }

        markDirty()
    }

    fun isFieldEnabled(projector: FieldProjectorTile): Boolean {
        return projectorProps(projector).field != null
    }

    fun getOwningProjector(fieldBlockPos: BlockPos): Int? {
        return projectorPropsById.firstNotNullOfOrNull { (id, props) ->
            if (props.field?.contains(fieldBlockPos) == true) id else null
        }
    }

    fun getCoreId(core: ForceFieldCoreTile): CoreId {
        var coreId = cores.inverse[core.pos]

        if (coreId == null) {
            coreId = nextCoreId

            nextCoreId = CoreId(nextCoreId.value + 1)
            cores[coreId] = core.pos

            markDirty()
        }

        return coreId
    }

    fun removeCore(core: ForceFieldCoreTile) {
        cores.inverse.remove(core.pos)
        markDirty()
    }

    private fun projectorIdOrNull(projector: FieldProjectorTile): Int? {
        return projectorPositionById.inverse[projector.pos]
    }

    private fun projectorId(projector: FieldProjectorTile): Int {
        return projectorIdOrNull(projector) ?: error("Cannot get ID of unregistered projector at ${projector.pos}")
    }

    private fun projectorPropsOrNull(projector: FieldProjectorTile): ProjectorProps? {
        return projectorIdOrNull(projector)?.let { projectorPropsById[it] }
    }

    private fun projectorProps(projector: FieldProjectorTile): ProjectorProps {
        return projectorPropsById[projectorId(projector)] ?: error("Projector at ${projector.pos} is unregistered, cannot get its props")
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        try {
            val state = MFFSMod.nbt.decode<State>(nbt)

            projectorPositionById.clear()
            projectorPropsById.clear()
            cores.clear()

            state.projectors.forEach { (id, savedProjectorProps) ->
                projectorPositionById[id] = savedProjectorProps.pos
                projectorPropsById[id] = savedProjectorProps.props
            }

            state.cores.forEach { (id, pos) ->
                cores[id] = pos
            }

            nextCoreId = state.nextCoreId
        } catch (error: Exception) {
            MFFSMod.logger.error("Error whilst restoring FieldManager state: ", error)
        }
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val state = State(
            projectors = projectorPositionById.mapValues { (id, pos) ->
                State.SavedProjectorProps(
                    pos = pos,
                    props = projectorPropsById[id]!!
                )
            },
            cores = cores,
            nextCoreId = nextCoreId,
        )

        val serializedState = MFFSMod.nbt.encode(state) as NBTTagCompound
        compound.merge(serializedState)

        return compound
    }

    @Serializable
    private data class State(
        val projectors: Map<Int, SavedProjectorProps>,
        val cores: Map<CoreId, @Serializable(BlockPosSerializer::class) BlockPos>,
        val nextCoreId: CoreId,
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

@JvmInline
@Serializable
value class CoreId(val value: Int)
