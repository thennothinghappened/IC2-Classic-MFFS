package mods.orca.mffs.blocks.field

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mods.orca.mffs.FieldManager.Companion.fieldManager
import mods.orca.mffs.MFFSMod
import mods.orca.mffs.ProjectorId
import mods.orca.mffs.blocks.IHasItemBlock
import mods.orca.mffs.blocks.base.BlockTileEntity
import mods.orca.mffs.blocks.utils.serializedStateOrNull
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt


object ProjectorBlock : BlockTileEntity<ProjectorTile>(ProjectorTile::class, Material.IRON), IHasItemBlock {

    private const val NAME = "projector"

    override val itemBlock = ItemBlock(this).apply {
        setRegistryName(NAME)
    }

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setHardness(3f)
        setResistance(50f)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {

        val projector = getTileEntity(worldIn, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        val powered = worldIn.isBlockPowered(pos)

        when {
            powered && !projector.active -> projector.activateField()
            !powered && projector.active -> projector.deactivateField()
            else -> Unit
        }

    }

    override fun onBlockClicked(worldIn: World, pos: BlockPos, playerIn: EntityPlayer) {
        val projector = getTileEntity(worldIn, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        projector.testExpandingTheField()
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {

        val projector = getTileEntity(worldIn, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        projector.onDestroy()
        super.breakBlock(worldIn, pos, state)

    }

    override fun createTileEntity(world: World, state: IBlockState): ProjectorTile {
        return ProjectorTile(FieldPerimeterSdf.Sphere())
    }

}

/**
 * A signed-distance function producing a given field shape.
 */
@Serializable
sealed interface FieldPerimeterSdf {

    /**
     * Calculate the signed distance to the edge of this field shape.
     */
    operator fun invoke(x: Int, y: Int, z: Int): Double

    /**
     * Half of the length, width and height of the field. Determines the area that encompasses the SDF.
     */
    val halfSize: Vec3i

    @Serializable
    data class Sphere(val radius: Int = 5) : FieldPerimeterSdf {

        override fun invoke(x: Int, y: Int, z: Int): Double = sqrt(
            x.toDouble().pow(2) +
                    y.toDouble().pow(2) +
                    z.toDouble().pow(2)
        ) - radius.toDouble()

        override val halfSize: Vec3i
            get() = Vec3i(radius, radius, radius)

    }

    @Serializable
    data class Cube(val radius: Int = 5) : FieldPerimeterSdf {

        override fun invoke(x: Int, y: Int, z: Int): Double = maxOf(x, y, z).toDouble() - radius.toDouble()

        override val halfSize: Vec3i
            get() = Vec3i(radius, radius, radius)

    }

}

class ProjectorTile(sdf: FieldPerimeterSdf) : TileEntity() {

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
        require(!active) {"Field must NOT be already active when activating it"}
        createField()
        active = true
    }

    fun deactivateField() {
        require(active) {"Field must be active when deactivating it"}
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
