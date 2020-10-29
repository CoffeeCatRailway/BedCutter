package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.client.renderer.entity.layers.ToggleVillagerLevelPendantLayer;
import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.common.command.HasHeadCommand;
import coffeecatrailway.bedcutter.network.CutterMessageHandler;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.block.BedBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CutterMod.MOD_ID)
public class CutterMod
{
    public static final String MOD_ID = "bedcutter";
    public static final Logger LOGGER = getLogger("");

    public static CutterConfig SERVER_CONFIG;
    public static Registrate REGISTRATE;

    public CutterMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setupClient);
        modEventBus.addListener(this::setupCommon);

        final Pair<CutterConfig, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(CutterConfig::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.getRight());
        SERVER_CONFIG = server.getLeft();
        LOGGER.debug("Register config");

        MinecraftForge.EVENT_BUS.register(this);

        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> ItemGroup.DECORATIONS)
                .addDataGenerator(ProviderType.BLOCK_TAGS, new RegistrateProviders.TagBlocks())
                .addDataGenerator(ProviderType.ITEM_TAGS, new RegistrateProviders.TagItems())
                .addDataGenerator(ProviderType.LANG, new RegistrateProviders.Lang());

        CutterRegistry.load();
    }

    private void setupClient(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            Minecraft mc = event.getMinecraftSupplier().get();
            EntityRenderer<?> renderer = mc.getRenderManager().renderers.get(EntityType.VILLAGER);
            if (!(renderer instanceof VillagerRenderer))
                return;

            VillagerRenderer villagerRenderer = (VillagerRenderer) renderer;
            for (LayerRenderer<?, ?> layer : villagerRenderer.layerRenderers)
            {
                if (ToggleVillagerLevelPendantLayer.isToggle(layer) && mc.getResourceManager() instanceof IReloadableResourceManager)
                {
                    villagerRenderer.layerRenderers.remove(layer);
                    villagerRenderer.layerRenderers.add(new ToggleVillagerLevelPendantLayer<>(villagerRenderer, (IReloadableResourceManager) mc.getResourceManager(), "villager"));
                    LOGGER.debug("Swapped villager 'VillagerLevelPendantLayer' with 'ToggleVillagerLevelPendantLayer'");
                    return;
                }
            }
        });
    }

    private void setupCommon(FMLCommonSetupEvent event)
    {
        CutterRegistry.CUTTERS.stream().flatMap(block -> block.get().getStateContainer().getValidStates().stream())
                .filter(state -> state.get(BedBlock.PART) == BedPart.HEAD).forEach(state -> PointOfInterestType.POIT_BY_BLOCKSTATE.put(state, PointOfInterestType.HOME));
        LOGGER.debug("Added cutter beds to PointOfInterestType::BED_HEADS");

        HasHeadCapability.register();
        CutterMessageHandler.init();
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event)
    {
        HasHeadCommand.register(event.getDispatcher());
        LOGGER.info("Registered command(s)");
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(CutterMod.MOD_ID, path);
    }

    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(MOD_ID + (!StringUtils.isNullOrEmpty(name) ? "-" + name : ""));
    }
}
