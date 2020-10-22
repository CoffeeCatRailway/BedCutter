package coffeecatrailway.bedcutter;

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
import net.minecraftforge.fml.common.Mod;

@Mod(CutterMod.MOD_ID)
public class CutterMod
{
    public static final String MOD_ID = "bedcutter";

    public static Registrate REGISTRATE;

    public CutterMod()
    {
        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> ItemGroup.DECORATIONS)
                .addDataGenerator(ProviderType.BLOCK_TAGS, new TagBlocks())
                .addDataGenerator(ProviderType.ITEM_TAGS, new TagItems())
                .addDataGenerator(ProviderType.LANG, new NonNullConsumer<RegistrateLangProvider>()
                {
                    @Override
                    public void accept(RegistrateLangProvider provider)
                    {
                        String attackLang = "death.attack." + CutterRegistry.BED_CUTTER_DAMAGE.getDamageType();
                        provider.add(attackLang, "%1$s got their head chopped off");
                        provider.add(attackLang + ".player", "%2$s chopped %1$s's head off");
                    }
                });

        MinecraftForge.EVENT_BUS.register(this);

        CutterRegistry.load();
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(CutterMod.MOD_ID, path);
    }

    public static class TagBlocks implements NonNullConsumer<RegistrateTagsProvider<Block>>
    {
        public static final ITag.INamedTag<Block> CUTTER_BEDS = BlockTags.createOptional(CutterMod.getLocation("cutter_beds"));

        @Override
        public void accept(RegistrateTagsProvider<Block> provider)
        {
            provider.getOrCreateBuilder(BlockTags.BEDS).addTag(CUTTER_BEDS);
        }
    }

    public static class TagItems implements NonNullConsumer<RegistrateTagsProvider<Item>>
    {
        public static final ITag.INamedTag<Item> CUTTER_BEDS = ItemTags.createOptional(CutterMod.getLocation("cutter_beds"));

        @Override
        public void accept(RegistrateTagsProvider<Item> provider)
        {
            provider.getOrCreateBuilder(ItemTags.BEDS).addTag(CUTTER_BEDS);
        }
    }
}
