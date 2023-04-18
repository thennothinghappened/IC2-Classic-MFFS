package mods.orca.mffs.recipe;

import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.item.IC2Items;
import mods.orca.mffs.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipes {
    public static void init() {
        ClassicRecipes.advCrafting.addRecipe(new ItemStack(ModItems.FREQCARD_BLANK),
                "PPP",
                        "PCP",
                        "PPP",
                'P', Items.PAPER, 'C', IC2Items.getItem("crafting", "circuit"));
    }
}
