package coffeecatrailway.bedcutter.common.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author CoffeeCatRailway
 * Created: 20/06/2021
 */
public class CutterBedBlock extends BedBlock implements ISortInTab
{
    public CutterBedBlock(DyeColor dye, Properties properties)
    {
        super(dye, properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items)
    {
        ISortInTab.super.fillItemCategory(tab, items);
    }

    @Override
    public boolean isType(ItemStack stack)
    {
        return stack.getItem() instanceof BedItem;
    }

    @Override
    public CreativeModeTab getCreativeTab()
    {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return RenderShape.MODEL;
    }

    //    @Override
//    public BlockEntity newBlockEntity(BlockGetter blockGetter)
//    {
//        return null;
//    }

//    @Override
//    public final boolean isEntityBlock() {
//        return false;
//    }
}
