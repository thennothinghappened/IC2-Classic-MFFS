package mods.orca.mffs;

import mods.orca.mffs.proxy.Proxy;
import mods.orca.mffs.recipe.ModRecipes;
import mods.orca.mffs.util.handlers.ModGuiHandler;
import mods.orca.mffs.util.handlers.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = MFFSMod.modId,
    name = "Modular Force Field System",
    dependencies = "required-after:ic2",
    useMetadata = true
)
public class MFFSMod {

    public final static String modId = "mffs";
    public static Logger logger;

    @Mod.Instance(modId)
    public static MFFSMod instance;

    public MFFSMod() {

        instance = this;

        MinecraftForge.EVENT_BUS.register(RegistryHandler.class);

    }

    @SidedProxy(
        clientSide = "mods.orca.mffs.proxy.ClientProxy",
        serverSide = "mods.orca.mffs.proxy.Proxy"
    )
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("Welcome to Force-fields! (" + MFFSMod.modId + " is now loading)");

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
