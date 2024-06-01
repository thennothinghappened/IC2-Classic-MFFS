package mods.orca.mffs.items

import mods.orca.mffs.client.MFFSTab
import net.minecraft.item.Item

open class ItemBase @JvmOverloads constructor(
    protected var name: String,
    setCreativeTab: Boolean = true
) : Item() {

    private fun addToCreativeTab() {
        setCreativeTab(MFFSTab)
    }

    private fun register() {
        ModItems.ITEMS.add(this)
    }

    init {

        translationKey = "mffs.$name"
        setRegistryName(name)

        if (setCreativeTab) {
            addToCreativeTab()
        }

        register()

    }

}
