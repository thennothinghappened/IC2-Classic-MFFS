package mods.orca.mffs.blocks.upgrades.camoflage;

import mods.orca.mffs.blocks.base.tile.TileHasInventory;
import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;
import mods.orca.mffs.blocks.upgrades.IUpgrade;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileUpgradeCamo extends TileHasInventory implements IUpgrade {

    public TileUpgradeCamo() {
        inventory = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                // we only want blocks, no point trying to camo an item.
                return stack.getItem() instanceof ItemBlock;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public boolean compatible(TileUpgradableMachine machine) {
        return false;
    }
}
