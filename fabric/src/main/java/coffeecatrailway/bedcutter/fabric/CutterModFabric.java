package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.CutterConfig;
import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.common.command.HasHeadCommand;
import coffeecatrailway.bedcutter.common.block.CutterBedBlock;
import coffeecatrailway.bedcutter.util.EventUtil;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;

public class CutterModFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CutterMod.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> HasHeadCommand.register(dispatcher));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> EventUtil.addCutterBedsPoi(Registry.BLOCK.stream(), state -> PoiType.TYPE_BY_STATE.put(state, PoiType.HOME)));

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resources) -> {
            AutoConfig.getConfigHolder(CutterConfig.class).load();
            CutterMod.LOGGER.info("Server config reloaded");
        });
    }
}
