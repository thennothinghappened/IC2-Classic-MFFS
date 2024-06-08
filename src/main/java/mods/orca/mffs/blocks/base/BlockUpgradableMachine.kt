package mods.orca.mffs.blocks.base

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.reflect.KClass

abstract class BlockUpgradableMachine<TE : TileEntity>(
    tileEntityClass: KClass<TE>
) : BlockMachine<TE>(tileEntityClass) {

    override fun onBlockAdded(world: World, pos: BlockPos, state: IBlockState) {
//        getTileEntity(world, pos)
//            ?.run { scanForUpgrades() }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun neighborChanged(
        state: IBlockState,
        world: World,
        thisPos: BlockPos,
        fromBlock: Block,
        fromPos: BlockPos
    ) {
        if (!world.isRemote) {
//            getTileEntity(world, thisPos)
//                ?.run { scanForUpgrades() }
        }
    }

}
