package mods.orca.mffs.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Hook into the Event Bus that forwards on to Kotlin registry events to set up the mod's items and whatnot.
 */
@Mod.EventBusSubscriber
public class RegistryHandlerHook {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        RegistryHandler.onItemRegister(event);
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        RegistryHandler.onBlockRegister(event);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        RegistryHandler.onModelRegister(event);
    }

}
