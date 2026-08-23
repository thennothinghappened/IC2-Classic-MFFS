package mods.orca.mffs.container

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot

/**
 * Container with the standard arrangement of player slots.
 *
 * @param playerInventory The inventory instance of the player using the container.
 * @param inventoryOffsetY The vertical offset in the container before the inventory.
 */
abstract class ContainerWithPlayerInventory(
    val playerInventory: InventoryPlayer,
    protected val inventoryOffsetY: Int
) : ContainerBase(playerInventory.mainInventory.size) {
    init {
        for (y in 0..2) {
            for (x in 0..8) {
                addSlotToContainer(Slot(
                    playerInventory,
                    x + y * 9 + 9,
                    8 + x * 18,
                    inventoryOffsetY + y * 18
                ))
            }
        }

        for (x in 0..8) {
            addSlotToContainer(Slot(
                playerInventory,
                x,
                8 + x * 18,
                inventoryOffsetY + (3 * 18) + 4
            ))
        }
    }
}
