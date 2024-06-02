package mods.orca.mffs.items

import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemFreqcard : ItemBase("freqcard", false) {

    companion object {
        const val KEY_CORE_POS: String = "corePos"
    }

    override fun addInformation(
        stack: ItemStack,
        worldIn: World?,
        tooltip: MutableList<String>,
        flagIn: ITooltipFlag
    ) {

        // TODO: Rewrite this system to track cores by ID rather than world pos, which may be considerably slow.

        stack.tagCompound
            ?.takeIf { it.hasKey(KEY_CORE_POS) }
            ?.run { getIntArray(KEY_CORE_POS) }
            ?.run { tooltip.add("Linked to core: ${get(0)}, ${get(1)}, ${get(2)}") }

    }

}
