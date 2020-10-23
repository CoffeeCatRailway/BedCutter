package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.common.command.HasHeadCommand;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
        final Pair<CutterConfig, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(CutterConfig::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.getRight());
        SERVER_CONFIG = server.getLeft();
        LOGGER.info("Register config");

        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> ItemGroup.DECORATIONS)
                .addDataGenerator(ProviderType.BLOCK_TAGS, new RegistrateProviders.TagBlocks())
                .addDataGenerator(ProviderType.ITEM_TAGS, new RegistrateProviders.TagItems())
                .addDataGenerator(ProviderType.LANG, new RegistrateProviders.Lang());

        MinecraftForge.EVENT_BUS.register(this);

        CutterRegistry.load();
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event)
    {
        HasHeadCommand.register(event.getServer().getCommandManager().getDispatcher());
        LOGGER.info("Registered command(s)");
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
