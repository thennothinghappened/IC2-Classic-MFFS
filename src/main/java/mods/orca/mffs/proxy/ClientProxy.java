package mods.orca.mffs.proxy;

import mods.orca.mffs.MFFSMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends Proxy {
    @Override
    public void registerItemRenderer(Item item, int metadata, String id) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(
                item.getRegistryName(), id
        ));
    }
}
