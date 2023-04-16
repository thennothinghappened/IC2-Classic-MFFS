package mods.orca.mffs.items;

import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
    public ItemBase(String name) {
        setUnlocalizedName("mffs." + name);
        setRegistryName(name);

        ItemInit.ITEMS.add(this);
    }

    public ItemBase(String name, CreativeTabs creativeTab) {
        setUnlocalizedName("mffs." + name);
        setRegistryName(name);
        setCreativeTab(creativeTab);

        ItemInit.ITEMS.add(this);
    }


    @Override
    public void registerModels() {
        MFFSMod.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
