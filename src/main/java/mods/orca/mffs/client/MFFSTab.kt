package mods.orca.mffs.client

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.core.BlockForceFieldCore
import mods.orca.mffs.items.ItemFrequencyCard
import mods.orca.mffs.items.ItemFrequencyCardBlank
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

/**
 * The creative tab used for our mod items.
 */
object MFFSTab : CreativeTabs(MFFSMod.modId) {
    fun setup() {
        BlockForceFieldCore.creativeTab = this
        ItemFrequencyCardBlank.creativeTab = this
    }

    override fun createIcon(): ItemStack {
        return ItemStack(ItemFrequencyCard)
    }
}
