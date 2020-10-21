package coffeecatrailway.bedcutter;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

/**
 * @author CoffeeCatRailway
 * Created: 21/10/2020
 */
public class CutterTags
{
    public static class Blocks implements NonNullConsumer<RegistrateTagsProvider<Block>>
    {
        @Override
        public void accept(RegistrateTagsProvider<Block> provider)
        {
        }
    }

    public static class Items implements NonNullConsumer<RegistrateTagsProvider<Item>>
    {
        @Override
        public void accept(RegistrateTagsProvider<Item> provider)
        {
        }
    }
}
