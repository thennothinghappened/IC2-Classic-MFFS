package mods.orca.mffs.proxy

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader

class ClientProxy : Proxy() {

    override fun registerItemRenderer(item: Item, metadata: Int, id: String) {
        ModelLoader.setCustomModelResourceLocation(
            item,
            metadata,
            ModelResourceLocation(
                requireNotNull(item.registryName),
                id
            )
        )
    }

}
