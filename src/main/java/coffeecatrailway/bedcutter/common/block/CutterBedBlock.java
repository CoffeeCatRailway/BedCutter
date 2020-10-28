package coffeecatrailway.bedcutter.common.block;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BedItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IBlockReader;

/**
 * @author CoffeeCatRailway
 * Created: 21/10/2020
 */
public class CutterBedBlock extends BedBlock implements ISortInTab
{
    public CutterBedBlock(Properties properties)
    {
        super(DyeColor.WHITE, properties);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        ISortInTab.super.fillItemGroup(group, items);
    }

    @Override
    public boolean isType(ItemStack stack)
    {
        return stack.getItem() instanceof BedItem;
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return null;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
