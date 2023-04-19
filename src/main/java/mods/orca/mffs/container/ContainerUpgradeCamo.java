package mods.orca.mffs.container;

import mods.orca.mffs.blocks.upgrades.camoflage.TileUpgradeCamo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerUpgradeCamo extends ContainerPlayerInvBase {

    public ContainerUpgradeCamo(InventoryPlayer inventoryPlayer, final TileUpgradeCamo upgradeCamo) {

        super(inventoryPlayer);

        IItemHandler inventory = upgradeCamo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        addSlotToContainer(new SlotItemHandler(inventory, 0, 80, 35) {
            @Override
            public void onSlotChanged() {
                upgradeCamo.markDirty();
            }
        });
    }

}
