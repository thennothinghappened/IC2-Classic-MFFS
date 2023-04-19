package mods.orca.mffs.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerPlayerInvBase extends ContainerBase {
    public ContainerPlayerInvBase(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 9; j ++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k ++) {
            addSlotToContainer(new Slot(inventoryPlayer, k, 8 + k * 18, 142));
        }

        oursStartIndex = inventoryPlayer.mainInventory.size();
    }
}
