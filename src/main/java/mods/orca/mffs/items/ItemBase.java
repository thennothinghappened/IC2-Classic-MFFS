package mods.orca.mffs.items;

import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.client.MFFSTab;
import mods.orca.mffs.util.IHasModel;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    protected String name;

    public ItemBase(String name, Boolean setCreativeTab) {
        this.name = name;

        setUnlocalizedName("mffs." + name);
        setRegistryName(name);

        if (setCreativeTab) setCreativeTab(MFFSTab.mffsTab);

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name) {
        this(name, true);
    }

    @Override
    public void registerItemModel() {
        MFFSMod.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
