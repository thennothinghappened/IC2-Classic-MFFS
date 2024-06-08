package mods.orca.mffs.recipe

import ic2.api.classic.recipe.ClassicRecipes
import ic2.api.item.IC2Items
import mods.orca.mffs.blocks.core.BlockForceFieldCore
import mods.orca.mffs.items.ItemFrequencyCard
import mods.orca.mffs.items.ItemFrequencyCardBlank
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

/**
 * Handler for initializing the recipes we add.
 */
object RecipeHandler {

    /**
     * Setup our recipes!
     */
    fun setup() {
        addItemRecipes()
        addBlockRecipes()
    }

    private fun addItemRecipes() {

        ClassicRecipes.advCrafting.addRecipe(
            ItemStack(ItemFrequencyCardBlank),

            "PPP",
            "PCP",
            "PPP",

            'P', Items.PAPER,
            'C', IC2Items.getItem("crafting", "circuit")
        )

        // Convert a card back to a blank.
        ClassicRecipes.advCrafting.addShapelessRecipe(
            ItemStack(ItemFrequencyCardBlank),
            ItemStack(ItemFrequencyCard)
        )

    }

    private fun addBlockRecipes() {

        ClassicRecipes.advCrafting.addRecipe(
            ItemStack(BlockForceFieldCore),

            "WAW",
            "CFC",
            "WEW",

            'W', IC2Items.getItem("cell", "electrolyzed"),
            'A', IC2Items.getItem("crafting", "alloy"),
            'C', IC2Items.getItem("crafting", "circuit"),
            'F', IC2Items.getItem("frequency_transmitter"),
            'E', IC2Items.getItem("te", "electrolyzer")
        )

    }

}
