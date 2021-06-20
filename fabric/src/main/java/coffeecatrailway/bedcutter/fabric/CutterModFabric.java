package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.CutterConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class CutterModFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CutterMod.init();
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resources) -> {
            AutoConfig.getConfigHolder(CutterConfig.class).load();
            CutterMod.LOGGER.info("Server config reloaded");
        });
    }
}
