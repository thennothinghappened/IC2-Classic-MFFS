package mods.orca.mffs.items

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.client.MFFSTab
import net.minecraft.item.Item

/**
 * Blank frequency card, which can be converted into one linked to a given core for using its power for a projector!
 */
object ItemFrequencyCardBlank : Item() {

    private const val NAME = "frequency_card_blank"

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setCreativeTab(MFFSTab)
    }

}
