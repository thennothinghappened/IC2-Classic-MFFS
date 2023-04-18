package mods.orca.mffs.client;

import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MFFSTab extends CreativeTabs {

    public static final MFFSTab mffsTab = new MFFSTab();

    public MFFSTab() {
        super(MFFSMod.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.FREQCARD_BLANK);
    }
}
