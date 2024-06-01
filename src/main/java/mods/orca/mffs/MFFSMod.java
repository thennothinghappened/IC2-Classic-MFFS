package mods.orca.mffs;

import mods.orca.mffs.proxy.Proxy;
import mods.orca.mffs.recipe.ModRecipes;
import mods.orca.mffs.util.handlers.ModGuiHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = MFFSMod.MODID,
        name = MFFSMod.NAME,
        version = MFFSMod.VERSION,
        dependencies = "required-after:ic2",
        useMetadata = true
)
public class MFFSMod {
    public static final String MODID = "mffs";
    public static final String NAME = "Modular Force Field System";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @Mod.Instance(MODID)
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
        logger.info("Loading " + NAME);

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
