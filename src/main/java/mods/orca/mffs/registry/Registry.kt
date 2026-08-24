package mods.orca.mffs.registry

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.field.ForceFieldBlock
import mods.orca.mffs.blocks.BlockWithItem
import mods.orca.mffs.blocks.base.BlockWithEntity
import mods.orca.mffs.blocks.core.ForceFieldCoreBlock
import mods.orca.mffs.blocks.euinjector.EuInjectorBlock
import mods.orca.mffs.blocks.projector.FieldProjectorBlock
import mods.orca.mffs.client.MFFSTab
import mods.orca.mffs.items.ItemFrequencyCard
import mods.orca.mffs.items.ItemFrequencyCardBlank
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import kotlin.collections.forEach
import kotlin.reflect.full.memberProperties

/**
 * Handler to register our set of blocks and items and so forth with Forge.
 */
@Suppress("unused")
object Registry {
    val creativeTab = MFFSTab()

    object Blocks {
        val forceField = ForceFieldBlock()
        val forceFieldCore = ForceFieldCoreBlock()
        val fieldProjector = FieldProjectorBlock()
        val euInjector = EuInjectorBlock()
    }

    object Items {
        val frequencyCard = ItemFrequencyCard()
        val frequencyCardBlank = ItemFrequencyCardBlank()
    }

    /**
     * Register our list of items.
     */
    @JvmStatic
    @SubscribeEvent
    fun onItemRegister(event: RegistryEvent.Register<Item>) {
        Items::class.memberProperties
            .mapNotNull { it.get(Items) as? Item }
            .forEach(event.registry::register)

        // Register accompanying [ItemBlock]s for blocks which use them.
        Blocks::class.memberProperties
            .mapNotNull { it.get(Blocks) as? BlockWithItem }
            .forEach { event.registry.register(it.itemBlock) }
    }

    /**
     * Register our list of blocks.
     */
    @JvmStatic
    @SubscribeEvent
    fun onBlockRegister(event: RegistryEvent.Register<Block>) {
        Blocks::class.memberProperties
            .mapNotNull { it.get(Blocks) as? Block }
            .forEach(event.registry::register)

        Blocks::class.memberProperties
            .mapNotNull { it.get(Blocks) as? BlockWithEntity<*> }
            .forEach { GameRegistry.registerTileEntity(it.tileEntityClass.java, it.registryName) }
    }

    /**
     * Register model renderers for our items and blocks.
     */
    @JvmStatic
    @SubscribeEvent
    fun onModelRegister(event: ModelRegistryEvent) {
        Items::class.memberProperties
            .mapNotNull { it.get(Items) as? Item }
            .forEach {
                MFFSMod.proxy.registerItemRenderer(it, 0, "inventory")
            }

        Blocks::class.memberProperties
            .mapNotNull { it.get(Blocks) as? Block }
            .forEach {
                MFFSMod.proxy.registerItemRenderer(Item.getItemFromBlock(it), 0, "inventory")
            }
    }

}
