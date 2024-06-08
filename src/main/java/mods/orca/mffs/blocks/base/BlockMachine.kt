package mods.orca.mffs.blocks.base

import ic2.api.tile.IWrenchable
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.reflect.KClass

abstract class BlockMachine<TE : TileEntity>(
    tileEntityClass: KClass<TE>
) : BlockTileEntity<TE>(
    tileEntityClass,
    Material.IRON
), IWrenchable {

    init {
        setHardness(3f)
        setResistance(50f)
        soundType = SoundType.METAL
    }

    override fun getFacing(world: World, pos: BlockPos): EnumFacing {
        return EnumFacing.NORTH
    }

    override fun setFacing(world: World, pos: BlockPos, newDirection: EnumFacing, player: EntityPlayer): Boolean {
        return false
    }

    override fun wrenchCanRemove(world: World, pos: BlockPos, player: EntityPlayer): Boolean {
        return true
    }

    protected open fun getExtraDrops(
        drops: NonNullList<ItemStack>,
        world: World,
        pos: BlockPos,
        state: IBlockState
    ) {}

    // Add a chance for the machine to drop things like its internal inventory.
    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {

        val extraDrops = NonNullList.create<ItemStack>()
        getExtraDrops(extraDrops, world, pos, state)

        extraDrops.forEach { drop ->
            val item = EntityItem(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), drop)
            world.spawnEntity(item)
        }

        super.breakBlock(world, pos, state)
    }

    // our wrench drops are the same as if the block is destroyed normally.
    override fun getWrenchDrops(
        world: World,
        pos: BlockPos,
        state: IBlockState,
        te: TileEntity,
        player: EntityPlayer,
        fortune: Int
    ): List<ItemStack> {

        val drops = NonNullList.create<ItemStack>()
        getDrops(drops, world, pos, state, fortune)

        return drops
    }

}
