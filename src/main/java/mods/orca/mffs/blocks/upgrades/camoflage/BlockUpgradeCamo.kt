package mods.orca.mffs.blocks.upgrades.camoflage

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.base.BlockMachine
import mods.orca.mffs.blocks.upgrades.IBlockUpgrade
import mods.orca.mffs.util.handlers.ModGuiHandler
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler

class BlockUpgradeCamo : BlockMachine<TileUpgradeCamo>(
    TileUpgradeCamo::class.java,
    "upgrade_camo"
), IBlockUpgrade {

    override fun createTileEntity(world: World, state: IBlockState): TileUpgradeCamo {
        return TileUpgradeCamo()
    }

    override fun onBlockActivated(
        world: World,
        pos: BlockPos,
        state: IBlockState,
        player: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {

        if (world.isRemote) {
            return true
        }

        val tile = getTileEntity(world, pos)
            ?: return false

        val heldItem = player.getHeldItem(hand)

        if (player.isSneaking) {

            player.openGui(
                MFFSMod.instance,
                ModGuiHandler.GuiId.CamouflageUpgrade.ordinal,
                world,
                pos.x,
                pos.y,
                pos.z
            )

        } else {

            val itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)
                ?: throw IllegalStateException("Camo Upgrade block must have an item handler, but it is missing!")

            if (heldItem.isEmpty) {
                player.setHeldItem(hand, itemHandler.extractItem(0, 1, false))
            } else {
                player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false))
            }

            tile.markDirty()

        }

        return true
    }

    override fun getExtraDrops(
        drops: NonNullList<ItemStack>,
        world: World,
        pos: BlockPos,
        state: IBlockState
    ) {

        val tile = getTileEntity(world, pos)
            ?: return

        val itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
            ?: throw IllegalStateException("Camo Upgrade block must have an item handler, but it is missing!")

        val stack = itemHandler.getStackInSlot(0)

        if (!stack.isEmpty) {
            drops.add(stack)
        }

    }

}
