package coffeecatrailway.bedcutter.common.block;

import coffeecatrailway.bedcutter.common.tileentity.VillagerHeadTileEntity;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 24/10/2020
 */
public class VillagerWallHeadBlock extends WallSkullBlock
{
    public VillagerWallHeadBlock(Properties properties)
    {
        super(VillagerHeadBlock.Types.VILLAGER, properties);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return new VillagerHeadTileEntity();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        CutterRegistry.VILLAGER_HEAD.get().onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        return CutterRegistry.VILLAGER_HEAD.get().getDrops(state, builder);
    }
}
