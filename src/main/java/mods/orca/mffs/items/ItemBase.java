package mods.orca.mffs.items;

import mods.orca.mffs.client.MFFSTab;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name, Boolean setCreativeTab) {
        this.name = name;

        setTranslationKey("mffs." + name);
        setRegistryName(name);

        if (setCreativeTab) setCreativeTab(MFFSTab.mffsTab);

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name) {
        this(name, true);
    }
}
