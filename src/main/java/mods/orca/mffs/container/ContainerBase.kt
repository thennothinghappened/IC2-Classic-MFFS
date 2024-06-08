package mods.orca.mffs.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

/**
 * Our base shared behaviour for a GUI container.
 *
 * @property ourFirstSlotIndex The index of the first slot for this GUI.
 */
abstract class ContainerBase(private val ourFirstSlotIndex: Int = 0) : Container() {

    /**
     * Container with the standard arrangement of player slots.
     *
     * @param inventoryPlayer The inventory instance of the player using the container.
     * @param inventoryOffsetY The vertical offset in the container before the inventory.
     */
    constructor(
        inventoryPlayer: InventoryPlayer,
        inventoryOffsetY: Int
    ) : this(inventoryPlayer.mainInventory.size) {

        for (y in 0..2) {
            for (x in 0..8) {
                addSlotToContainer(
                    Slot(
                        inventoryPlayer,
                        x + y * 9 + 9,
                        8 + x * 18,
                        inventoryOffsetY + y * 18
                    )
                )
            }
        }

        for (x in 0..8) {
            addSlotToContainer(
                Slot(
                    inventoryPlayer,
                    x,
                    8 + x * 18,
                    inventoryOffsetY + (3 * 18) + 4
                )
            )
        }

    }

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
