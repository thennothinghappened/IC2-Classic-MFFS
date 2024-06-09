package mods.orca.mffs.items

import mods.orca.mffs.MFFSMod
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
object ItemFrequencyCard : Item() {

    private const val NAME = "frequency_card"

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

        // TODO: Rewrite this system to track cores by ID rather than world pos, which may be considerably slow.

        stack.tagCompound
            ?.takeIf { it.hasKey(NBTKey.CorePos.name) }
            ?.run { getIntArray(NBTKey.CorePos.name) }
            ?.run { tooltip.add("Linked to core: ${get(0)}, ${get(1)}, ${get(2)}") }

    }

    enum class NBTKey {
        CorePos
    }

}
