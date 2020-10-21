package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
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
import net.minecraftforge.fml.common.Mod;

@Mod(CutterMod.MOD_ID)
public class CutterMod
{
    public static final String MOD_ID = "bedcutter";

    public static Registrate REGISTRATE;

    public CutterMod()
    {
        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> ItemGroup.DECORATIONS)
                .addDataGenerator(ProviderType.BLOCK_TAGS, new CutterTags.Blocks())
                .addDataGenerator(ProviderType.ITEM_TAGS, new CutterTags.Items());

        MinecraftForge.EVENT_BUS.register(this);

        CutterRegistry.load();
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(CutterMod.MOD_ID, path);
    }

    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(CutterMod.MOD_ID + "-" + name);
    }
}
