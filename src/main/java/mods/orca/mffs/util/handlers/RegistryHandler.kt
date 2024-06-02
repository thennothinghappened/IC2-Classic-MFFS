package mods.orca.mffs.util.handlers

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.ModBlocks
import mods.orca.mffs.blocks.base.BlockTileEntity
import mods.orca.mffs.items.ModItems
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Handler to register our set of blocks and items and so forth with Forge.
 */
object RegistryHandler {

    /**
     * Register our list of items.
     */
    @JvmStatic
    fun onItemRegister(event: RegistryEvent.Register<Item>) {
        ModItems.ITEMS.forEach {
            event.registry.register(it)
        }
    }

    /**
     * Register our list of blocks.
     */
    @JvmStatic
    fun onBlockRegister(event: RegistryEvent.Register<Block>) {

        ModBlocks.BLOCKS.forEach(event.registry::register)

        ModBlocks.BLOCKS
            .filterIsInstance<BlockTileEntity<*>>()
            .forEach {
                GameRegistry.registerTileEntity(it.tileEntityClass, it.registryName)
            }

    }

    /**
     * Register model renderers for our items and blocks.
     */
    @JvmStatic
    fun onModelRegister(event: ModelRegistryEvent) {

        ModItems.ITEMS
            .forEach {
                MFFSMod.proxy.registerItemRenderer(it, 0, "inventory")
            }

        ModBlocks.BLOCKS
            .forEach {
                MFFSMod.proxy.registerItemRenderer(Item.getItemFromBlock(it), 0, "inventory")
            }

    }

}
