package mods.orca.mffs.container

import mods.orca.mffs.MFFSMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack

/**
 * Our base shared behaviour for a GUI container.
 *
 * @property ourFirstSlotIndex The index of the first slot for this GUI.
 */
abstract class ContainerBase(
    @JvmField
    protected val ourFirstSlotIndex: Int = 0
) : Container() {

    override fun canInteractWith(player: EntityPlayer) = true

    /**
     * Handler for a shift-click on a slot.
     *
     * This implementation attempts to distribute the item contents over any available slots
     * in the opposing container that may accept this item type, filling their stacks as much as possible per slot.
     *
     * TODO: What is the return value of this even used for? Always using [ItemStack.EMPTY] seems to cause no issues...
     */
    override fun transferStackInSlot(player: EntityPlayer, index: Int): ItemStack {

        val slotToMoveFrom = inventorySlots[index]
            ?.takeIf { it.hasStack }
            ?: return ItemStack.EMPTY

        val hasMovedItems = if (index >= ourFirstSlotIndex) {

            // Moving from a slot that belongs to us to a slot in the other container.
            mergeItemStack(slotToMoveFrom.stack, 0, ourFirstSlotIndex, true)

        } else {

            // Moving from a slot that belongs to the other container, to our slots.
            mergeItemStack(slotToMoveFrom.stack, ourFirstSlotIndex, inventorySlots.size, false)

        }

        if (hasMovedItems) {
            slotToMoveFrom.onSlotChanged()
        }

        return ItemStack.EMPTY

    }
}
