package mods.orca.mffs;

import mods.orca.mffs.proxy.Proxy;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = MFFSMod.MODID, name = MFFSMod.NAME, version = MFFSMod.VERSION, dependencies = "required-after:ic2")
public class MFFSMod {
    public static final String MODID = "mffs";
    public static final String NAME = "Modular Force Field System";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @Mod.Instance
    public static MFFSMod instance;

    public MFFSMod() { instance = this; }

    @SidedProxy(
            clientSide = "mods.orca.mffs.proxy.ClientProxy",
            serverSide = "mods.orca.mffs.proxy.Proxy"
    )
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
