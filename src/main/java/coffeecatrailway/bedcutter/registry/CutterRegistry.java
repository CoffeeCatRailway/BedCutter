package coffeecatrailway.bedcutter.registry;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.RegistrateProviders;
import coffeecatrailway.bedcutter.client.renderer.tileentity.VillagerHeadTileEntityRenderer;
import coffeecatrailway.bedcutter.client.renderer.tileentity.itemstack.VillagerHeadItemStackTERenderer;
import coffeecatrailway.bedcutter.common.block.CutterBedBlock;
import coffeecatrailway.bedcutter.common.block.VillagerHeadBlock;
import coffeecatrailway.bedcutter.common.block.WallVillagerHeadBlock;
import coffeecatrailway.bedcutter.common.item.VillagerHeadItem;
import coffeecatrailway.bedcutter.common.tileentity.VillagerHeadTileEntity;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
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
     * Misc
     */
    public static final DamageSource BED_CUTTER_DAMAGE = new DamageSource("bed_cutter").setDamageBypassesArmor().setDamageIsAbsolute();

    /*
     * Blocks
     */
    public static final Set<RegistryEntry<CutterBedBlock>> CUTTERS = new HashSet<>();

    public static final RegistryEntry<CutterBedBlock> WHITE_BED_CUTTER = cutterBed("white", () -> Blocks.WHITE_WOOL, () -> Items.WHITE_BED);
    public static final RegistryEntry<CutterBedBlock> ORANGE_BED_CUTTER = cutterBed("orange", () -> Blocks.ORANGE_WOOL, () -> Items.ORANGE_BED);
    public static final RegistryEntry<CutterBedBlock> MAGENTA_BED_CUTTER = cutterBed("magenta", () -> Blocks.MAGENTA_WOOL, () -> Items.MAGENTA_BED);
    public static final RegistryEntry<CutterBedBlock> LIGHT_BLUE_BED_CUTTER = cutterBed("light_blue", () -> Blocks.LIGHT_BLUE_WOOL, () -> Items.LIGHT_BLUE_BED);
    public static final RegistryEntry<CutterBedBlock> YELLOW_BED_CUTTER = cutterBed("yellow", () -> Blocks.YELLOW_WOOL, () -> Items.YELLOW_BED);
    public static final RegistryEntry<CutterBedBlock> LIME_BED_CUTTER = cutterBed("lime", () -> Blocks.LIME_WOOL, () -> Items.LIME_BED);
    public static final RegistryEntry<CutterBedBlock> PINK_BED_CUTTER = cutterBed("pink", () -> Blocks.PINK_WOOL, () -> Items.PINK_BED);
    public static final RegistryEntry<CutterBedBlock> GRAY_BED_CUTTER = cutterBed("gray", () -> Blocks.GRAY_WOOL, () -> Items.GRAY_BED);
    public static final RegistryEntry<CutterBedBlock> LIGHT_GRAY_BED_CUTTER = cutterBed("light_gray", () -> Blocks.LIGHT_GRAY_WOOL, () -> Items.LIGHT_GRAY_BED);
    public static final RegistryEntry<CutterBedBlock> CYAN_BED_CUTTER = cutterBed("cyan", () -> Blocks.CYAN_WOOL, () -> Items.CYAN_BED);
    public static final RegistryEntry<CutterBedBlock> PURPLE_BED_CUTTER = cutterBed("purple", () -> Blocks.PURPLE_WOOL, () -> Items.PURPLE_BED);
    public static final RegistryEntry<CutterBedBlock> BLUE_BED_CUTTER = cutterBed("blue", () -> Blocks.BLUE_WOOL, () -> Items.BLUE_BED);
    public static final RegistryEntry<CutterBedBlock> BROWN_BED_CUTTER = cutterBed("brown", () -> Blocks.BROWN_WOOL, () -> Items.BROWN_BED);
    public static final RegistryEntry<CutterBedBlock> GREEN_BED_CUTTER = cutterBed("green", () -> Blocks.GREEN_WOOL, () -> Items.GREEN_BED);
    public static final RegistryEntry<CutterBedBlock> RED_BED_CUTTER = cutterBed("red", () -> Blocks.RED_WOOL, () -> Items.RED_BED);
    public static final RegistryEntry<CutterBedBlock> BLACK_BED_CUTTER = cutterBed("black", () -> Blocks.BLACK_WOOL, () -> Items.BLACK_BED);

    public static final RegistryEntry<WallVillagerHeadBlock> VILLAGER_WALL_HEAD = REGISTRATE.object("villager_wall_head").block(WallVillagerHeadBlock::new)
            .initialProperties(() -> Blocks.PLAYER_HEAD).addLayer(() -> RenderType::getCutoutMipped)
            .loot((tables, block) -> tables.registerLootTable(block, LootTable.builder())).setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models().withExistingParent(ctx.getName(), "block/skull"))).register();
    public static final RegistryEntry<VillagerHeadBlock> VILLAGER_HEAD = REGISTRATE.object("villager_head").block(VillagerHeadBlock::new).initialProperties(() -> Blocks.PLAYER_HEAD)
            .loot((tables, block) -> tables.registerLootTable(block, LootTable.builder().addLootPool(LootPool.builder().rolls(new RandomValueRange(1))
                    .addEntry(ItemLootEntry.builder(block).acceptCondition(SurvivesExplosion.builder())
                            .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
                            .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                    .replaceOperation("type", "type")
                                    .replaceOperation("profession", "profession"))))))
            .addLayer(() -> RenderType::getCutoutMipped)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models().withExistingParent(ctx.getName(), "block/skull")))
            .item((block, prop) -> new VillagerHeadItem(block, VILLAGER_WALL_HEAD.get(), prop)).properties(prop -> prop.setISTER(() -> () -> VillagerHeadItemStackTERenderer.INSTANCE))
            .tag(Tags.Items.HEADS).model((ctx, provider) -> provider.withExistingParent(ctx.getName(), "item/template_skull")
                    .transforms().transform(ModelBuilder.Perspective.HEAD).rotation(0f, 180f, 0f).translation(0f, 7f, 0f).scale(2f)).build().register();

    /*
     * Tile Entities
     */
    public static final RegistryEntry<TileEntityType<VillagerHeadTileEntity>> VILLAGER_HEAD_TILE = REGISTRATE.tileEntity("villager_head", (NonNullFunction<TileEntityType<VillagerHeadTileEntity>, VillagerHeadTileEntity>) VillagerHeadTileEntity::new)
            .renderer(() -> VillagerHeadTileEntityRenderer::new).validBlocks(CutterRegistry.VILLAGER_HEAD, CutterRegistry.VILLAGER_WALL_HEAD).register();

    private static RegistryEntry<CutterBedBlock> cutterBed(String colorId, Supplier<IItemProvider> wool, Supplier<IItemProvider> bed)
    {
        RegistryEntry<CutterBedBlock> cutter = REGISTRATE.object(colorId + "_bed_cutter").block(CutterBedBlock::new).initialProperties(() -> Blocks.RED_BED).tag(BlockTags.BEDS)
                .loot((tables, block) -> tables.registerLootTable(block, LootTable.builder().addLootPool(LootPool.builder().acceptCondition(SurvivesExplosion.builder()).addEntry(ItemLootEntry.builder(block)
                        .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(BedBlock.PART, BedPart.HEAD)))))))
                .tag(RegistrateProviders.TagBlocks.CUTTER_BEDS).recipe((ctx, provider) -> {
                    ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).addCriterion("has_wool", RegistrateRecipeProvider.hasItem(wool.get()))
                            .addCriterion("has_cutter", RegistrateRecipeProvider.hasItem(Blocks.STONECUTTER)).setGroup("bed")
                            .key('p', ItemTags.PLANKS).key('w', wool.get()).key('c', Blocks.STONECUTTER)
                            .patternLine("c  ").patternLine("www").patternLine("ppp").build(provider, CutterMod.getLocation(ctx.getName() + "_mirror"));

                    ShapelessRecipeBuilder.shapelessRecipe(ctx.getEntry()).addCriterion("has_bed", RegistrateRecipeProvider.hasItem(bed.get()))
                            .addCriterion("has_cutter", RegistrateRecipeProvider.hasItem(Blocks.STONECUTTER)).setGroup("bed")
                            .addIngredient(bed.get()).addIngredient(Blocks.STONECUTTER).build(provider, CutterMod.getLocation(ctx.getName() + "_with_bed"));
                }).blockstate((ctx, provider) -> {
                    ModelFile front = provider.models().withExistingParent(ctx.getName() + "_front", CutterMod.getLocation("block/bed_cutter_front")).texture("bed", new ResourceLocation("entity/bed/" + colorId));
                    ModelFile back = provider.models().withExistingParent(ctx.getName() + "_back", CutterMod.getLocation("block/bed_cutter_back")).texture("bed", new ResourceLocation("entity/bed/" + colorId));

                    provider.getVariantBuilder(ctx.getEntry()) // North
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.NORTH)
                            .modelForState().modelFile(front).rotationY(180).addModel()
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.NORTH)
                            .modelForState().modelFile(back).rotationY(180).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.NORTH)
                            .modelForState().modelFile(front).rotationY(180).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.NORTH)
                            .modelForState().modelFile(back).rotationY(180).addModel()

                            // South
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.SOUTH)
                            .modelForState().modelFile(front).addModel()
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.SOUTH)
                            .modelForState().modelFile(back).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.SOUTH)
                            .modelForState().modelFile(front).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.SOUTH)
                            .modelForState().modelFile(back).addModel()

                            // East
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.EAST)
                            .modelForState().modelFile(front).rotationY(270).addModel()
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.EAST)
                            .modelForState().modelFile(back).rotationY(270).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.EAST)
                            .modelForState().modelFile(front).rotationY(270).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.EAST)
                            .modelForState().modelFile(back).rotationY(270).addModel()

                            // West
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.WEST)
                            .modelForState().modelFile(front).rotationY(90).addModel()
                            .partialState().with(BedBlock.OCCUPIED, false).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.WEST)
                            .modelForState().modelFile(back).rotationY(90).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.HEAD).with(BedBlock.HORIZONTAL_FACING, Direction.WEST)
                            .modelForState().modelFile(front).rotationY(90).addModel()
                            .partialState().with(BedBlock.OCCUPIED, true).with(BedBlock.PART, BedPart.FOOT).with(BedBlock.HORIZONTAL_FACING, Direction.WEST)
                            .modelForState().modelFile(back).rotationY(90).addModel();
                }).addLayer(() -> RenderType::getCutoutMipped).item().properties(prop -> prop.maxStackSize(1)).tag(RegistrateProviders.TagItems.CUTTER_BEDS)
                .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), CutterMod.getLocation("item/bed_cutter_item"))
                        .texture("bed", new ResourceLocation("entity/bed/" + colorId)).assertExistence()).build().register();
        CUTTERS.add(cutter);
        return cutter;
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}
