package coffeecatrailway.bedcutter.registry;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.CutterTags;
import coffeecatrailway.bedcutter.common.block.CutterBedBlock;
import coffeecatrailway.bedcutter.common.block.tile.CutterBedTileEntity;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

import static coffeecatrailway.bedcutter.CutterMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 21/10/2020
 */
public class CutterRegistry
{
    private static final Logger LOGGER = CutterMod.getLogger("Registry");

    /*
     * Blocks
     */

    /*
     * Tile Entities
     */

    public static void load()
    {
        LOGGER.info("Loaded");
    }
}
