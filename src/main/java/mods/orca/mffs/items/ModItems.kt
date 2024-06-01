package mods.orca.mffs.items

import net.minecraft.item.Item

object ModItems {

    @JvmField
    val ITEMS: MutableList<Item> = ArrayList()

    @JvmField
    val FREQCARD_BLANK: Item = ItemBase("freqcard_blank")

    @JvmField
    val FREQCARD: Item = ItemFreqcard()

}
