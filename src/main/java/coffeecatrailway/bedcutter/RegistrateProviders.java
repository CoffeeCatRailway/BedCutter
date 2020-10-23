package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

/**
 * @author CoffeeCatRailway
 * Created: 22/10/2020
 */
public class RegistrateProviders
{
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

    public static class Lang implements NonNullConsumer<RegistrateLangProvider>
    {
        @Override
        public void accept(RegistrateLangProvider provider)
        {
            String attackLang = "death.attack." + CutterRegistry.BED_CUTTER_DAMAGE.getDamageType();
            provider.add(attackLang, "%1$s got their head chopped off");
            provider.add(attackLang + ".player", "%2$s chopped %1$s's head off");

            provider.add("commands.has_head.get.has", "%1$s has a head");
            provider.add("commands.has_head.get.hasnt", "%1$s hasn't got a head");

            provider.add("commands.has_head.set.has", "%1$s has a new head");
            provider.add("commands.has_head.set.hasnt", "%1$s no longer has a head");

            provider.add("config." + CutterMod.MOD_ID + ".block.cutter_damage_type", "Cutter Damage Type");
        }
    }
}
