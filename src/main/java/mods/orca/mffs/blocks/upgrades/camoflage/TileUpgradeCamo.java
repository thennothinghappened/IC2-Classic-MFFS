package mods.orca.mffs.blocks.upgrades.camoflage;

import mods.orca.mffs.blocks.base.tile.TileHasInventory;
import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;
import mods.orca.mffs.blocks.projector.TileProjector;
import mods.orca.mffs.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileUpgradeCamo extends TileHasInventory implements ICamoUpgrade {

    private TileProjector owner;

    public TileUpgradeCamo() {
        inventory = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                // we only want blocks, no point trying to camo an item.
                return BlockUtils.isValidCamo(stack);
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

    @Nullable
    @Override
    public TileUpgradableMachine getOwner() {
        return owner;
    }

    @Override
    public void setOwner(@Nullable TileUpgradableMachine owner) {
        this.owner = (TileProjector) owner;
    }

    @Nullable
    @Override
    public Block getBlockMask() {
        Item item = inventory.getStackInSlot(0).getItem();
        if (item == Items.AIR) return null;

        return ((ItemBlock) item).getBlock();
    }
}
