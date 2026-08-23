package mods.orca.mffs.items

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.utils.getIntOrNull
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * A linked frequency card, with a given core attached. This card allows us to link projectors to cores remotely to
 * power them wirelessly.
 */
class ItemFrequencyCard : Item() {
    companion object {
        private const val NAME = "frequency_card"
    }

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<String>,
        flag: ITooltipFlag
    ) {
        val coreId = stack.tagCompound?.getIntOrNull(NBTKey.CoreId.name)

        if (coreId != null) {
            tooltip.add("Linked to core $coreId")
        }
    }

    enum class NBTKey {
        CoreId
    }
}
