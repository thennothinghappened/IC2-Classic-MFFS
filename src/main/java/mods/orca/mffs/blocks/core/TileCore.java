package mods.orca.mffs.blocks.core;

import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;
import mods.orca.mffs.blocks.upgrades.ICoreUpgrade;
import mods.orca.mffs.blocks.upgrades.IUpgrade;
import mods.orca.mffs.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileCore extends TileUpgradableMachine {

    public TileCore() {
        super(10000000);

        inventory = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == ModItems.FREQCARD_BLANK;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
            }
        };
    }

    @Override
    public int getSinkTier() {
        return 3;
    }

    @Override
    protected boolean upgradeCompatible(IUpgrade upgrade) {
        return upgrade instanceof ICoreUpgrade;
    }
}
