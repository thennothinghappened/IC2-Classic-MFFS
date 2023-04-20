package mods.orca.mffs.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockUtils {

    public static Block getBlock(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemBlock)) return Blocks.AIR;
        return ((ItemBlock) itemStack.getItem()).getBlock();
    }

    public static boolean isValidCamo(Block block) {
        return block.getBlockState().getBaseState().isFullBlock();
    }

    public static boolean isValidCamo(ItemStack itemStack) {
        return isValidCamo(getBlock(itemStack));
    }
}
