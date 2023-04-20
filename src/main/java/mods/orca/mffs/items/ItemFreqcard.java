package mods.orca.mffs.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFreqcard extends ItemBase {
    public static final String KEY_CORE_POS = "corePos";

    public ItemFreqcard() {
        super("freqcard", false);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(KEY_CORE_POS)) {
            int[] corePos = stack.getTagCompound().getIntArray(KEY_CORE_POS);
            tooltip.add("Linked to core: " + corePos[0] + ", " +corePos[1] + ", " + corePos[2]);
        }
    }
}
