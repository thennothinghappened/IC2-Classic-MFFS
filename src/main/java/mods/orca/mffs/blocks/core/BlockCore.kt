package mods.orca.mffs.blocks.core

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.base.BlockUpgradableMachine
import mods.orca.mffs.blocks.upgrades.IBlockUpgrade
import mods.orca.mffs.items.ItemFreqcard
import mods.orca.mffs.items.ModItems
import mods.orca.mffs.util.handlers.ModGuiHandler
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BlockCore : BlockUpgradableMachine<TileCore>("core") {

    override fun getTileEntityClass(): Class<TileCore> {
        return TileCore::class.java
    }

    override fun createTileEntity(world: World, state: IBlockState): TileCore {
        MFFSMod.logger.debug("Created a new Core!")
        return TileCore()
    }

    /**
     * Converts a stack of blank MFFS cards into an equivalent amount of 'burnt' cards which the projector
     * can use to figure out where this Core is.
     *
     * @param stack Incoming blank cards
     * @param pos Position of this block
     * @return A 'burnt' MFFS card that points to this block position to put into a projector.
     */
    fun burnMFFSCard(stack: ItemStack, pos: BlockPos): ItemStack? {

        if (stack.isEmpty) {
            return null
        }

        if (stack.item !== ModItems.FREQCARD_BLANK) {
            return null
        }

        val nbt = NBTTagCompound().apply {
            setIntArray(ItemFreqcard.KEY_CORE_POS, intArrayOf(pos.x, pos.y, pos.z))
        }

        return ItemStack(ModItems.FREQCARD, stack.count, 0)
            .apply { tagCompound = nbt }

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

        val heldItem = playerIn.getHeldItem(hand)
        val burntCards = burnMFFSCard(heldItem, pos)

        // they clicked with a blank MFFS card, so lets give them burnt one(s)
        if (burntCards != null) {
            if (!worldIn.isRemote) {
                playerIn.setHeldItem(hand, burntCards)
            }
            return true
        }

        // if the player is trying to place an upgrade, don't get in the way
        if (heldItem.item is ItemBlock) {
            if ((heldItem.item as ItemBlock).block is IBlockUpgrade) {
                return false
            }
        }

        playerIn.openGui(MFFSMod.instance, ModGuiHandler.GuiId.Core.ordinal, worldIn, pos.x, pos.y, pos.z)
        return true

    }
}
