package coffeecatrailway.bedcutter.common.block;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * @author CoffeeCatRailway
 * Created: 21/10/2020
 */
public class CutterBedBlock extends BedBlock
{
    public CutterBedBlock(Properties properties)
    {
        super(DyeColor.WHITE, properties);
    }

    /**
     * Based on: https://github.com/Ocelot5836/Sonar/blob/1.16.3/src/main/java/io/github/ocelot/sonar/common/item/SpawnEggItemBase.java#L48
     */
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (group == ItemGroup.DECORATIONS)
        {
            if (items.stream().anyMatch(stack -> stack.getItem() instanceof BedItem))
            {
                Optional<ItemStack> optional = items.stream().filter(stack -> stack.getItem() instanceof BedItem).reduce((a, b) -> b);
                if (optional.isPresent() && items.contains(optional.get()))
                {
                    items.add(items.indexOf(optional.get()) + 1, new ItemStack(this));
                    return;
                }
            }
            items.add(new ItemStack(this));
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (world.isRemote)
        {
            return ActionResultType.CONSUME;
        } else
        {
            if (state.get(PART) != BedPart.HEAD)
            {
                pos = pos.offset(state.get(HORIZONTAL_FACING));
                state = world.getBlockState(pos);
                if (!state.isIn(this))
                {
                    return ActionResultType.CONSUME;
                }
            }

            if (!doesBedWork(world))
            {
                world.removeBlock(pos, false);
                BlockPos blockpos = pos.offset(state.get(HORIZONTAL_FACING).getOpposite());
                if (world.getBlockState(blockpos).isIn(this))
                {
                    world.removeBlock(blockpos, false);
                }

                world.createExplosion(null, DamageSource.func_233546_a_(), null, (double) pos.getX() + .5d, (double) pos.getY() + .5d, (double) pos.getZ() + .5d, 5f, true, Explosion.Mode.DESTROY);
                return ActionResultType.SUCCESS;
            } else if (state.get(OCCUPIED))
            {
                if (!this.tryWakeUpVillager(world, pos))
                {
                    player.sendStatusMessage(new TranslationTextComponent("block.minecraft.bed.occupied"), true);
                }

                return ActionResultType.SUCCESS;
            } else
            {
                player.trySleep(pos).ifLeft((result) -> {
                    if (result != null)
                    {
                        player.sendStatusMessage(result.getMessage(), true);
                    }
                });
                return ActionResultType.SUCCESS;
            }
        }
    }

    private boolean tryWakeUpVillager(World world, BlockPos pos)
    {
        List<VillagerEntity> list = world.getEntitiesWithinAABB(VillagerEntity.class, new AxisAlignedBB(pos), LivingEntity::isSleeping);
        if (list.isEmpty())
        {
            return false;
        } else
        {
            list.get(0).wakeUp();
            return true;
        }
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
