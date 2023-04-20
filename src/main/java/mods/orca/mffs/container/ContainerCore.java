package mods.orca.mffs.container;

import mods.orca.mffs.blocks.core.TileCore;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCore extends ContainerPlayerInvBase {
    public ContainerCore(InventoryPlayer inventoryPlayer, final TileCore core) {

        super(inventoryPlayer);

        IItemHandler inventory = core.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        addSlotToContainer(new SlotItemHandler(inventory, 0, 80, 35) {
            @Override
            public void onSlotChanged() {
                core.markDirty();
            }
        });
    }
}
