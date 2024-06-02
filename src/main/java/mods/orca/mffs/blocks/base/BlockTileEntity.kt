package mods.orca.mffs.blocks.base

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

abstract class BlockTileEntity<TE : TileEntity>(
    val tileEntityClass: Class<TE>,
    name: String,
    material: Material,
    setCreativeTab: Boolean
) : BlockBase(name, material, setCreativeTab) {

    fun getTileEntity(world: IBlockAccess, pos: BlockPos): TE? =
        world.getTileEntity(pos)
            ?.takeIf { it::class.java == tileEntityClass }
            ?.let { it as TE }

    override fun hasTileEntity(state: IBlockState): Boolean {
        return true
    }

    abstract override fun createTileEntity(world: World, state: IBlockState): TE

}
