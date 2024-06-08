package mods.orca.mffs;

import mods.orca.mffs.proxy.Proxy;
import mods.orca.mffs.registry.RegistryHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * The main entry point for the MFFS mod!
 */
@SuppressWarnings("unused")
@Mod(
    modid = MFFSMod.modId,
    name = "Modular Force Field System",
    dependencies = "required-after:ic2",
    useMetadata = true
)
public class MFFSMod {

    public static final String modId = "mffs";
    public static Logger logger;

    @Mod.Instance(modId)
    public static MFFSMod instance;

    public MFFSMod() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(RegistryHandler.class);
    }

    @SidedProxy(
        clientSide = "mods.orca.mffs.proxy.ClientProxy",
        serverSide = "mods.orca.mffs.proxy.DedicatedProxy"
    )
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        logger = event.getModLog();
        logger.info("Welcome to Force-fields! (" + MFFSMod.modId + " is now loading)");

        proxy.preInit();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    /**
     * Get the translation key for the given path.
     *
     * @param path "Path" of the translation key elements.
     * @return Fully qualified translation key string.
     */
    public static String translationKey(@Nonnull final String... path) {
        return modId + "." + String.join(".", path);
    }

    public static ResourceLocation resource(@Nonnull final String path) {
        return new ResourceLocation(modId, path);
    }

}
