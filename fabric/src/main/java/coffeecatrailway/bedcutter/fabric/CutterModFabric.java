package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.CutterConfig;
import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.HasHeadHandler;
import coffeecatrailway.bedcutter.common.command.HasHeadCommand;
import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public class CutterModFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CutterMod.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> HasHeadCommand.register(dispatcher));

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resources) -> {
            AutoConfig.getConfigHolder(CutterConfig.class).load();
            CutterMod.LOGGER.info("Server config reloaded");
        });

        EntityEvent.ADD.register((entity, level) -> {
            if (entity instanceof ServerPlayer)
            {
                CutterNetwork.CHANNEL.sendToPlayer((ServerPlayer) entity, new SyncHasHeadMessage(HasHeadHandler.hasHead((ServerPlayer) entity), entity.getId()));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> CutterNetwork.CHANNEL.sendToPlayer(serverPlayer, new SyncHasHeadMessage(HasHeadHandler.hasHead(serverPlayer), serverPlayer.getId())));
        PlayerEvent.PLAYER_CLONE.register((original, serverPlayer, wasDeath) -> {
            if (!wasDeath)
                return;
            HasHeadHandler.setHasHead(serverPlayer, HasHeadHandler.hasHead(original));
        });
        // TODO: Find alternate to PlayerEvent.StartTracking
//        TickEvent.PLAYER_POST.register(player -> {
//
//        });
    }
}
