package coffeecatrailway.bedcutter.forge;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.CutterConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author CoffeeCatRailway
 * Created: 20/06/2021
 */
@Mod.EventBusSubscriber(modid = CutterMod.MOD_ID)
public class CommonEvents
{
    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event)
    {
        AutoConfig.getConfigHolder(CutterConfig.class).load();
        CutterMod.LOGGER.info("Server config reloaded");
    }
}
