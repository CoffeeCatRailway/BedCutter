package coffeecatrailway.bedcutter.forge;

import coffeecatrailway.bedcutter.CutterMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(CutterMod.MOD_ID)
public class CutterModForge
{
    public CutterModForge()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupClient);
        modBus.addListener(this::setupCommon);

        MinecraftForge.EVENT_BUS.register(this);

        CutterMod.init();
    }

    private void setupClient(final FMLClientSetupEvent event)
    {

    }

    private void setupCommon(final FMLCommonSetupEvent event)
    {

    }
}
