package coffeecatrailway.bedcutter.registry;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.common.block.CutterBedBlock;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * @author CoffeeCatRailway
 * Created: 20/06/2021
 */
public class CutterBlocks
{
    private static final Logger LOGGER = CutterMod.getLogger("Blocks");
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(CutterMod.MOD_ID, Registry.BLOCK_REGISTRY);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(CutterMod.MOD_ID, Registry.ITEM_REGISTRY);

    public static final Set<RegistrySupplier<CutterBedBlock>> BED_CUTTERS = new HashSet<RegistrySupplier<CutterBedBlock>>();

    public static final RegistrySupplier<CutterBedBlock> WHITE_BED_CUTTER = registerCutterBed(DyeColor.WHITE);
    public static final RegistrySupplier<CutterBedBlock> ORANGE_BED_CUTTER = registerCutterBed(DyeColor.ORANGE);
    public static final RegistrySupplier<CutterBedBlock> MAGENTA_BED_CUTTER = registerCutterBed(DyeColor.MAGENTA);
    public static final RegistrySupplier<CutterBedBlock> LIGHT_BLUE_BED_CUTTER = registerCutterBed(DyeColor.LIGHT_BLUE);
    public static final RegistrySupplier<CutterBedBlock> YELLOW_BED_CUTTER = registerCutterBed(DyeColor.YELLOW);
    public static final RegistrySupplier<CutterBedBlock> LIME_BED_CUTTER = registerCutterBed(DyeColor.LIME);
    public static final RegistrySupplier<CutterBedBlock> PINK_BED_CUTTER = registerCutterBed(DyeColor.PINK);
    public static final RegistrySupplier<CutterBedBlock> GRAY_BED_CUTTER = registerCutterBed(DyeColor.GRAY);
    public static final RegistrySupplier<CutterBedBlock> LIGHT_GRAY_BED_CUTTER = registerCutterBed(DyeColor.LIGHT_GRAY);
    public static final RegistrySupplier<CutterBedBlock> CYAN_BED_CUTTER = registerCutterBed(DyeColor.CYAN);
    public static final RegistrySupplier<CutterBedBlock> PURPLE_BED_CUTTER = registerCutterBed(DyeColor.PURPLE);
    public static final RegistrySupplier<CutterBedBlock> BLUE_BED_CUTTER = registerCutterBed(DyeColor.BLUE);
    public static final RegistrySupplier<CutterBedBlock> BROWN_BED_CUTTER = registerCutterBed(DyeColor.BROWN);
    public static final RegistrySupplier<CutterBedBlock> GREEN_BED_CUTTER = registerCutterBed(DyeColor.GREEN);
    public static final RegistrySupplier<CutterBedBlock> RED_BED_CUTTER = registerCutterBed(DyeColor.RED);
    public static final RegistrySupplier<CutterBedBlock> BLACK_BED_CUTTER = registerCutterBed(DyeColor.BLACK);

    private static RegistrySupplier<CutterBedBlock> registerCutterBed(DyeColor dye)
    {
        RegistrySupplier<CutterBedBlock> bed = BLOCKS.register(dye.getName() + "_bed_cutter", () -> new CutterBedBlock(dye, BlockBehaviour.Properties.copy(Blocks.RED_BED)));
        ITEMS.register(dye.getName() + "_bed_cutter", () -> new BlockItem(bed.get(), new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_DECORATIONS)));
        BED_CUTTERS.add(bed);
        return bed;
    }

    public static void load()
    {
        BLOCKS.register();
        ITEMS.register();
        LOGGER.debug("Loaded");
    }
}
