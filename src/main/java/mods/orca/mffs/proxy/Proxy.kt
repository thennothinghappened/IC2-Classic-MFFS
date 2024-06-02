package mods.orca.mffs.proxy

import net.minecraft.item.Item

abstract class Proxy {
    abstract fun registerItemRenderer(item: Item, metadata: Int, id: String)
}
