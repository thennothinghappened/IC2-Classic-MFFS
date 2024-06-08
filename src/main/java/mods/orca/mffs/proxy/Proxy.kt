package mods.orca.mffs.proxy

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.container.MFFSGuiHandler
import mods.orca.mffs.recipe.RecipeHandler
import net.minecraft.item.Item
import net.minecraftforge.fml.common.network.NetworkRegistry

/**
 * Proxy for exposing a common set of behaviours with side-specific implementations.
 */
abstract class Proxy {

    /**
     * Run on the mod's PreInit event.
     */
    open fun preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(MFFSMod.instance, MFFSGuiHandler)
    }

    /**
     * Run on the mod's Init event.
     */
    open fun init() {
        RecipeHandler.setup()
    }

    /**
     * Run after mod initialization.
     */
    open fun postInit() {

    }

    /**
     * Register a renderer for the given [Item].
     */
    open fun registerItemRenderer(item: Item, metadata: Int, id: String) {}



}
