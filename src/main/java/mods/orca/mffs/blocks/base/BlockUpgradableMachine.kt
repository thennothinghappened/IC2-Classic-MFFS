package mods.orca.mffs.blocks.base

import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BlockUpgradableMachine<TE : TileUpgradableMachine>(
    tileEntityClass: Class<TE>,
    name: String
) : BlockMachine<TE>(tileEntityClass, name) {

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        getTileEntity(worldIn, pos)!!.findUpgrades()
    }

    override fun neighborChanged(
        state: IBlockState,
        worldIn: World,
        thisPos: BlockPos,
        fromBlock: Block,
        fromPos: BlockPos
    ) {
        if (!worldIn.isRemote) {
            getTileEntity(worldIn, thisPos)!!.findUpgrades()
        }
    }

}