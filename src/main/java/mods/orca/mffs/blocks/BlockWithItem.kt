package mods.orca.mffs.blocks

import net.minecraft.item.ItemBlock

/**
 * A block which should have an [ItemBlock] registered for it.
 */
interface BlockWithItem {

    /**
     * The [ItemBlock] this block owns.
     */
    val itemBlock: ItemBlock

}
