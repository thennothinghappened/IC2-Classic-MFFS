package mods.orca.mffs.container

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot

abstract class ContainerPlayerInvBase(
    inventoryPlayer: InventoryPlayer,
    inventoryStartOffset: Int
) : ContainerBase(inventoryPlayer.mainInventory.size) {

    init {

        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, inventoryStartOffset + i * 18))
            }
        }

        for (k in 0..8) {
            addSlotToContainer(Slot(inventoryPlayer, k, 8 + k * 18, inventoryStartOffset + (3 * 18) + 4))
        }

    }

}
