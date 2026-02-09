package mods.orca.mffs.registry

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.field.ForceFieldBlock
import mods.orca.mffs.blocks.BlockWithItem
import mods.orca.mffs.blocks.base.BlockTileEntity
import mods.orca.mffs.blocks.core.BlockForceFieldCore
import mods.orca.mffs.blocks.projector.BlockFieldProjector
import mods.orca.mffs.client.MFFSTab
import mods.orca.mffs.items.ItemFrequencyCard
import mods.orca.mffs.items.ItemFrequencyCardBlank
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Handler to register our set of blocks and items and so forth with Forge.
 */
@Suppress("unused")
object RegistryHandler {

    /**
     * Set of blocks we know of to register.
     */
    private val blocks: Set<Block> = setOf(
        ForceFieldBlock,
        BlockForceFieldCore,
        BlockFieldProjector
    )

    /**
     * Set of items we know of to register.
     */
    private val items: Set<Item> = setOf(
        ItemFrequencyCard,
        ItemFrequencyCardBlank
    )

    /**
     * Register our list of items.
     */
    @JvmStatic
    @SubscribeEvent
    fun onItemRegister(event: RegistryEvent.Register<Item>) {
        items.forEach(event.registry::register)

        // Register accompanying [ItemBlock]s for blocks which use them.
        blocks
            .filterIsInstance<BlockWithItem>()
            .forEach { event.registry.register(it.itemBlock) }

        MFFSTab.setup()
    }

    /**
     * Register our list of blocks.
     */
    @JvmStatic
    @SubscribeEvent
    fun onBlockRegister(event: RegistryEvent.Register<Block>) {

        blocks.forEach(event.registry::register)

        blocks
            .filterIsInstance<BlockTileEntity<*>>()
            .forEach { GameRegistry.registerTileEntity(it.tileEntityClass.java, it.registryName) }

    }

    /**
     * Register model renderers for our items and blocks.
     */
    @JvmStatic
    @SubscribeEvent
    fun onModelRegister(event: ModelRegistryEvent) {

        items.forEach {
            MFFSMod.proxy.registerItemRenderer(it, 0, "inventory")
        }

        blocks.forEach {
            MFFSMod.proxy.registerItemRenderer(Item.getItemFromBlock(it), 0, "inventory")
        }

    }

}
