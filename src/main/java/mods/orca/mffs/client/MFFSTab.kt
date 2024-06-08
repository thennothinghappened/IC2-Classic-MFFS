package mods.orca.mffs.client

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.items.ItemFrequencyCard
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

/**
 * The creative tab used for our mod items.
 */
object MFFSTab : CreativeTabs(MFFSMod.modId) {

    override fun createIcon(): ItemStack {
        return ItemStack(ItemFrequencyCard)
    }

}
