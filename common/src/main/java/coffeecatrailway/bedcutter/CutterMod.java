package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.registry.CutterBlocks;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CutterMod
{
    public static final String MOD_ID = "bedcutter";
    public static final Logger LOGGER = getLogger("");

    public static CutterConfig SERVER_CONFIG;

    public static void init()
    {
        SERVER_CONFIG = AutoConfig.register(CutterConfig.class, Toml4jConfigSerializer::new).getConfig();
        CutterMod.LOGGER.info("Server config registered");

        CutterNetwork.init();
        CutterBlocks.load();
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(MOD_ID + (!StringUtil.isNullOrEmpty(name) ? "-" + name : ""));
    }
}
