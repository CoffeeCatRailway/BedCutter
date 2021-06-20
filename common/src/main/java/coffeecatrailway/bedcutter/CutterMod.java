package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.config.CutterConfig;
import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.network.SyncConfigMessage;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class CutterMod
{
    public static final String MOD_ID = "bedcutter";
    public static final Logger LOGGER = getLogger("");

    private static ConfigHolder<CutterConfig> LOCAL_SERVER_CONFIG;
    private static CutterConfig SYNCED_SERVER_CONFIG;

    public static Supplier<CutterConfig> SERVER_CONFIG = () -> Optional.ofNullable(SYNCED_SERVER_CONFIG).orElseGet(LOCAL_SERVER_CONFIG);

    public static void init()
    {
        LOCAL_SERVER_CONFIG = AutoConfig.register(CutterConfig.class, Toml4jConfigSerializer::new);

        CutterNetwork.init();
//        PlayerEvent.PLAYER_JOIN.register(player -> CutterNetwork.CHANNEL.sendToPlayer(player, new SyncConfigMessage(SERVER_CONFIG.get())));
    }

    public static void setSyncedServerConfig(CutterConfig syncedServerConfig)
    {
        SYNCED_SERVER_CONFIG = syncedServerConfig;
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
