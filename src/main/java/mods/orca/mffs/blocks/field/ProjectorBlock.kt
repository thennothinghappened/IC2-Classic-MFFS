package mods.orca.mffs.blocks.field

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.IHasItemBlock
import mods.orca.mffs.blocks.base.BlockTileEntity
import mods.orca.mffs.blocks.field.FieldPerimeterSdf.Companion.calculate
import mods.orca.mffs.utils.toBlockPos
import mods.orca.mffs.utils.toIntArray
import mods.orca.mffs.utils.toList
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.storage.WorldSavedData
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.*
import kotlin.math.absoluteValue
import kotlin.math.max
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
    }

    override fun onBlockActivated(
        worldIn: World,
        pos: BlockPos,
        state: IBlockState,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {

        if (hand != EnumHand.MAIN_HAND) {
            return false
        }

        val projector = getTileEntity(worldIn, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        projector.uwu()

        return false
    }

    override fun createTileEntity(world: World, state: IBlockState): ProjectorTile {
        return ProjectorTile(FieldPerimeterSdf.Sphere)
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
    fun calculate(x: Int, y: Int, z: Int): Double

    @Serializable
    data class Sphere(val radius: Int = 5) : FieldPerimeterSdf {
        override fun calculate(x: Int, y: Int, z: Int): Double = sqrt(
            x.toDouble().pow(2) +
                    y.toDouble().pow(2) +
                    z.toDouble().pow(2)
        ) - radius.toDouble()
    }

    @Serializable
    data class Square(val radius: Int = 5) : FieldPerimeterSdf {
        override fun calculate(x: Int, y: Int, z: Int): Double = maxOf(x, y, z).toDouble() - radius.toDouble()
    }

}

class ProjectorTile(private var sdf: FieldPerimeterSdf) : TileEntity() {

    // Constructor to make forge happy when restoring the entity.
    @Suppress("unused")
    constructor() : this(FieldPerimeterSdf.Sphere())

    fun uwu() {

        val radius = 8

        if (!hasWorld() || world.isRemote) {
            return
        }

        for (y in -radius..radius) {
            for (z in -radius..radius) {
                for (x in -radius..radius) {
                    if (sdf.calculate(x, y, z, radius).absoluteValue < 0.5) {
                        world.setBlockState(pos.add(x, y, z), ForceFieldBlock.defaultState)
                    }
                }
            }
        }

        FieldManager.get(world).sayHi()

    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)

        val sdfTypeName = compound.getString(NbtKey.SdfType.name)
        sdf = FieldPerimeterSdf.entries.firstOrNull { it.name == sdfTypeName } ?: FieldPerimeterSdf.Sphere()
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setString(NbtKey.SdfType.name, sdf.name)

        return compound
    }

    private enum class NbtKey {
        SdfType
    }

}

@JvmInline
value class ProjectorId(val id: Int) {
    constructor(id: String) : this(id.toInt())
}

/**
 * Per-level manager for force field cores, projectors, and whatnot.
 */
class FieldManager private constructor() : WorldSavedData(DATA_NAME) {

    /**
     * Map of projector IDs to where the given projector is located.
     */
    private val projectorPositions: MutableMap<ProjectorId, BlockPos> = mutableMapOf()

    /**
     * Map of projector IDs to the blocks making up their force-field.
     */
    private val fields: MutableMap<ProjectorId, MutableSet<BlockPos>> = mutableMapOf()

    companion object {

        private const val DATA_NAME: String = "${MFFSMod.modId}_FieldManager"

        /**
         * Retrieve the field manager for the current level.
         */
        fun get(world: World): FieldManager {
            val storage = checkNotNull(world.perWorldStorage) { "world.perWorldStorage was null!!" }
            var instance = storage.getOrLoadData(FieldManager::class.java, DATA_NAME) as FieldManager?

            if (instance == null) {
                instance = FieldManager()
                storage.setData(DATA_NAME, instance)
            }

            return instance
        }

    }

    fun sayHi() {
        MFFSMod.logger.info("hai!! uwu~~")
    }

    override fun readFromNBT(nbt: NBTTagCompound) {

        projectorPositions.clear()

        val positionsNbt = nbt.getCompoundTag(NbtKey.ProjectorPositions.name)

        for (projectorId in positionsNbt.keySet) {
            val pos = positionsNbt.getIntArray(projectorId)
            projectorPositions[ProjectorId(projectorId)] = pos.toBlockPos()
        }

        fields.clear()

        val fieldsNbt = nbt.getCompoundTag(NbtKey.Fields.name)

        for (projectorId in fieldsNbt.keySet) {

            val blocks = mutableSetOf<BlockPos>()

            positionsNbt.getIntArray(projectorId).asSequence().chunked(3).forEach { pos ->
                blocks.add(pos.toBlockPos())
            }

            fields[ProjectorId(projectorId)] = blocks

        }

    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {

        compound.setTag(NbtKey.ProjectorPositions.name, NBTTagCompound().apply {
            for ((projectorId, pos) in projectorPositions) {
                setIntArray(projectorId.toString(), pos.toIntArray())
            }
        })

        compound.setTag(NbtKey.Fields.name, NBTTagCompound().apply {
            for ((projectorId, fieldPositions) in fields) {
                // FIXME: non-ideal repeated conversions :P
                setIntArray(projectorId.toString(), fieldPositions.flatMap(BlockPos::toList).toIntArray())
            }
        })

        return compound

    }

    private enum class NbtKey {
        ProjectorPositions,
        Fields
    }

}
