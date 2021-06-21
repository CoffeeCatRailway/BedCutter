package coffeecatrailway.bedcutter.forge;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.registry.CutterBlocks;
import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.architectury.registry.forge.RenderTypesImpl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

@Mod(CutterMod.MOD_ID)
public class CutterModForge
{
    public CutterModForge()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(CutterMod.MOD_ID, modBus);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::setupCommon);

        MinecraftForge.EVENT_BUS.register(this);

        CutterMod.init();
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        RenderTypesImpl.register(RenderType.cutoutMipped(), CutterBlocks.BED_CUTTERS.stream().map(Supplier::get).toArray(Block[]::new));
    }

    private void setupCommon(final FMLCommonSetupEvent event)
    {

    }
}
