package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.CutterConfig;
import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.util.EventUtil;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class CutterModFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CutterMod.init();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> EventUtil.addCutterBedsPoi(Registry.BLOCK.stream(), state -> PoiType.TYPE_BY_STATE.put(state, PoiType.HOME)));

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resources) -> {
            AutoConfig.getConfigHolder(CutterConfig.class).load();
            CutterMod.LOGGER.info("Server config reloaded");
        });
    }
}
