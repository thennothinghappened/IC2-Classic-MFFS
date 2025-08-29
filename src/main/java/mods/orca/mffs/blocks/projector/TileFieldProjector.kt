package mods.orca.mffs.blocks.projector

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mods.orca.mffs.MFFSMod
import mods.orca.mffs.WorldFieldManager
import mods.orca.mffs.blocks.utils.serializedStateOrNull
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import kotlin.math.absoluteValue

class TileFieldProjector(sdf: FieldPerimeterSdf) : TileEntity() {
    var state = State(sdf)
        private set

    private var sdf: FieldPerimeterSdf
        get() = state.sdf
        set(value) {
            state = state.copy(sdf = value)
            markDirty()
        }

    fun isActive() = WorldFieldManager.getOrThrow(world).isFieldEnabled(this)

    // Constructor to make forge happy when restoring the entity.
    @Suppress("unused")
    constructor() : this(FieldPerimeterSdf.Sphere())

    override fun onLoad() {
        super.onLoad()

        if (!hasWorld() || world.isRemote) {
            return
        }

        WorldFieldManager.getOrCreate(world).registerProjector(this)
    }

    fun onDestroy() {
        if (!hasWorld() || world.isRemote) {
            return
        }

        val fieldManager = WorldFieldManager.getOrThrow(world)

        if (fieldManager.isFieldEnabled(this)) {
            deactivateField()
        }

        fieldManager.deregisterProjector(this)
    }

    fun activateField() {
        require(!isActive()) { "Field must NOT be already active when activating it" }
        WorldFieldManager.getOrThrow(world).enableField(this)
    }

    fun deactivateField() {
        require(isActive()) { "Field must be active when deactivating it" }
        WorldFieldManager.getOrThrow(world).disableField(this)
    }

    fun testExpandingTheField() {
        if (!hasWorld() || world.isRemote) {
            return
        }

        val fieldManager = WorldFieldManager.getOrThrow(world)
        val isFieldEnabled = isActive()

        if (isFieldEnabled) {
            fieldManager.disableField(this)
        }

        sdf = when (val sdf = this.sdf) {
            is FieldPerimeterSdf.Cube -> sdf.copy(radius = sdf.radius + 1)
            is FieldPerimeterSdf.Sphere -> sdf.copy(radius = sdf.radius + 1)
        }

        if (isFieldEnabled) {
            fieldManager.enableField(this)
        }

        markDirty()
    }

    fun getFieldShape(): Set<BlockPos> {
        val halfSize = sdf.halfSize
        val field = mutableSetOf<BlockPos>()

        for (y in -halfSize.y..halfSize.y) {
            for (z in -halfSize.z..halfSize.z) {
                for (x in -halfSize.x..halfSize.x) {
                    if (sdf(x, y, z).absoluteValue < 0.5) {
                        field.add(pos.add(x, y, z))
                    }
                }
            }
        }

        return field
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
        val sdf: FieldPerimeterSdf = FieldPerimeterSdf.Sphere()
    )
}
