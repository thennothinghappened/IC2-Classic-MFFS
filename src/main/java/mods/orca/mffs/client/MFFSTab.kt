package mods.orca.mffs.client

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.items.ModItems
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object MFFSTab : CreativeTabs(MFFSMod.modId) {

    override fun createIcon(): ItemStack {
        return ItemStack(ModItems.FREQCARD)
    }

}
