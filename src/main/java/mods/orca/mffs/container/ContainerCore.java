package mods.orca.mffs.container;

import mods.orca.mffs.blocks.ModBlocks;
import mods.orca.mffs.blocks.core.TileCore;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCore extends ContainerPlayerInvBase {
    public ContainerCore(InventoryPlayer inventoryPlayer, final TileCore core) {

        super(inventoryPlayer, 142);

        IItemHandler inventory = core.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        addSlotToContainer(new SlotItemHandler(inventory, 0, 97, 120) {
            @Override
            public void onSlotChanged() {
                if (getHasStack()) {
                    // they've put an MFFS card in.
                    // we run on server and client as otherwise client doesn't know it changed until we pull it out again.
                    ItemStack burnt = ModBlocks.CORE.burnMFFSCard(getStack(), core.getPos());

                    if (burnt != null) {
                        putStack(burnt);
                    }
                }

                core.markDirty();
            }
        });
    }
}
