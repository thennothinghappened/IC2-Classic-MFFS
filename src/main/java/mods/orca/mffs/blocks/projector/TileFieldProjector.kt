package mods.orca.mffs.blocks.projector

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mods.orca.mffs.FieldManager.Companion.fieldManager
import mods.orca.mffs.MFFSMod
import mods.orca.mffs.ProjectorId
import mods.orca.mffs.blocks.field.ForceFieldBlock
import mods.orca.mffs.blocks.utils.serializedStateOrNull
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import kotlin.math.absoluteValue

class TileFieldProjector(sdf: FieldPerimeterSdf) : TileEntity() {

    var id: ProjectorId? = null

    var state = State(sdf)
        private set

    private var sdf: FieldPerimeterSdf
        get() = state.sdf
        set(value) {
            state = state.copy(sdf = value)
            markDirty()
        }

    var active: Boolean
        get() = state.active
        private set(value) {
            state = state.copy(active = value)
            markDirty()
        }

    // Constructor to make forge happy when restoring the entity.
    @Suppress("unused")
    constructor() : this(FieldPerimeterSdf.Sphere())

    override fun onLoad() {

        super.onLoad()

        if (!hasWorld() || world.isRemote) {
            return
        }

        fieldManager.registerProjector(this)

    }

    fun onDestroy() {

        if (!hasWorld() || world.isRemote) {
            return
        }

        if (active) {
            deactivateField()
        }

        fieldManager.deregisterProjector(this)

    }

    fun activateField() {
        require(!active) { "Field must NOT be already active when activating it" }
        createField()
        active = true
    }

    fun deactivateField() {
        require(active) { "Field must be active when deactivating it" }
        destroyField()
        active = false
    }

    fun testExpandingTheField() {

        if (!hasWorld() || world.isRemote) {
            return
        }

        // FIXME: We should be using the field manager's tracking!
        if (active) {
            destroyField()
        }

        sdf = when (val sdf = this.sdf) {
            is FieldPerimeterSdf.Cube -> sdf.copy(radius = sdf.radius + 1)
            is FieldPerimeterSdf.Sphere -> sdf.copy(radius = sdf.radius + 1)
        }

        if (active) {
            createField()
        }

        markDirty()

    }

    private fun createField() {

        if (!hasWorld() || world.isRemote) {
            return
        }

        val halfSize = sdf.halfSize

        for (y in -halfSize.y..halfSize.y) {
            for (z in -halfSize.z..halfSize.z) {
                for (x in -halfSize.x..halfSize.x) {
                    if (sdf(x, y, z).absoluteValue < 0.5) {
                        world.setBlockState(pos.add(x, y, z), ForceFieldBlock.defaultState)
                    }
                }
            }
        }

    }

    private fun destroyField() {

        if (!hasWorld() || world.isRemote) {
            return
        }

        // FIXME: We should be using the field manager's tracking!
        val halfSize = sdf.halfSize

        for (y in -halfSize.y..halfSize.y) {
            for (z in -halfSize.z..halfSize.z) {
                for (x in -halfSize.x..halfSize.x) {
                    if (sdf(x, y, z).absoluteValue < 0.5) {
                        world.setBlockToAir(pos.add(x, y, z))
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun readFromNBT(compound: NBTTagCompound) {

        super.readFromNBT(compound)

        serializedStateOrNull?.let { serializedState ->
            try {
                state = MFFSMod.nbt.decode(serializedState)
                MFFSMod.logger.debug("Decoded State: {}", state)
            } catch (err: Exception) {
                MFFSMod.logger.error("Error decoding!: ", err)
            }
        }

    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {

        serializedStateOrNull = MFFSMod.nbt.encode(state)
        MFFSMod.logger.debug("Encoded State: {}", serializedStateOrNull)

        return super.writeToNBT(compound)


    }

    @Serializable
    data class State(
        val sdf: FieldPerimeterSdf = FieldPerimeterSdf.Sphere(),
        val active: Boolean = false
    )

}