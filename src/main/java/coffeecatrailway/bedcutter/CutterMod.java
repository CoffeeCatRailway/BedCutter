package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.common.command.HasHeadCommand;
import coffeecatrailway.bedcutter.network.CutterMessageHandler;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

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
        modEventBus.addListener(this::setupCommon);

        final Pair<CutterConfig, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(CutterConfig::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.getRight());
        SERVER_CONFIG = server.getLeft();
        LOGGER.info("Register config");

        MinecraftForge.EVENT_BUS.register(this);

        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> ItemGroup.DECORATIONS)
                .addDataGenerator(ProviderType.BLOCK_TAGS, new RegistrateProviders.TagBlocks())
                .addDataGenerator(ProviderType.ITEM_TAGS, new RegistrateProviders.TagItems())
                .addDataGenerator(ProviderType.LANG, new RegistrateProviders.Lang());

        CutterRegistry.load();
    }

    public void setupCommon(FMLCommonSetupEvent event)
    {
        CutterRegistry.CUTTERS.stream().flatMap(block -> block.get().getStateContainer().getValidStates().stream())
                .filter(state -> state.get(BedBlock.PART) == BedPart.HEAD).forEach(state -> PointOfInterestType.POIT_BY_BLOCKSTATE.put(state, PointOfInterestType.HOME));
        LOGGER.info("Added cutter beds to PointOfInterestType::BED_HEADS");

        HasHeadCapability.register();
        CutterMessageHandler.init();
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event)
    {
        HasHeadCommand.register(event.getServer().getCommandManager().getDispatcher());
        LOGGER.info("Registered command(s)");
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        if (!event.isWasDeath())
            return;
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        original.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(originalHandler -> player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> handler.setHasHead(originalHandler.hasHead())));
    }

    @SubscribeEvent
    public void onCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(HasHeadCapability.ID, new HasHeadCapability.Provider((PlayerEntity) event.getObject()));
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;
                    CutterMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncHasHeadMessage(handler.hasHead(), serverPlayer.getEntityId()));
                }
            });
        }
    }

    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        PlayerEntity player = event.getPlayer();

        if (player instanceof ServerPlayerEntity && target instanceof LivingEntity) {
            target.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> CutterMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncHasHeadMessage(handler.hasHead(), target.getEntityId())));
        }
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(CutterMod.MOD_ID, path);
    }

    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(MOD_ID + (!name.isEmpty() ? "-" + name : ""));
    }
}
