package mods.orca.mffs.blocks.base

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import kotlin.reflect.KClass

/**
 * A Block which owns a tile entity. This just class provides some helper methods for such blocks.
 */
abstract class BlockTileEntity<TE : TileEntity>(
    val tileEntityClass: KClass<TE>,
    material: Material
) : Block(material) {

    /**
     * Get the tile entity associated with this block at the given position, safely casting it to
     * the [TE] type, if the entity is found and valid.
     */
    @Suppress("UNCHECKED_CAST")
    fun getTileEntity(world: IBlockAccess, pos: BlockPos): TE? =
        world.getTileEntity(pos)
            ?.takeIf { it::class == tileEntityClass }
            ?.takeUnless { it.isInvalid }
            ?.let { it as TE }

    override fun hasTileEntity(state: IBlockState): Boolean {
        return true
    }

    abstract override fun createTileEntity(world: World, state: IBlockState): TE

}
