package mods.orca.mffs.util.handlers;

import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.blocks.base.BlockTileEntity;
import mods.orca.mffs.blocks.ModBlocks;
import mods.orca.mffs.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        registry.registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));

        ModBlocks.BLOCKS.forEach(block -> {
            if (block instanceof BlockTileEntity) {
                // we know that it'll always be this setup and registry name cannot be null, we don't need to worry about this
                //noinspection unchecked,rawtypes,DataFlowIssue
                GameRegistry.registerTileEntity(((BlockTileEntity)block).getTileEntityClass(), block.getRegistryName());
            }
        });
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {

        ModItems.ITEMS.forEach(item ->  {
            MFFSMod.proxy.registerItemRenderer(item, 0, "inventory");
        });

        ModBlocks.BLOCKS.forEach(block -> {
            MFFSMod.proxy.registerItemRenderer(Item.getItemFromBlock(block), 0, "inventory");
        });
    }
}
