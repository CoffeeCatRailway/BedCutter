package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.CutterConfig;
import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.HasHeadHandler;
import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

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
        // TODO: Find alternate to PlayerEvent.StartTracking
//        TickEvent.PLAYER_POST.register(player -> {
//
//        });
    }
}
