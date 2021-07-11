package coffeecatrailway.bedcutter.util;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.common.block.CutterBedBlock;
import coffeecatrailway.bedcutter.registry.CutterMisc;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author CoffeeCatRailway
 * Created: 25/06/2021
 */
public final class EventUtil
{
    public EventUtil()
    {
    }

    public static void playerWakeUp(Player player, Level level, Runnable hasHeadRunnable)
    {
        player.getSleepingPos().ifPresent(pos -> {
            if (!(level.getBlockState(pos).getBlock() instanceof CutterBedBlock))
                return;

            if (!level.isClientSide())
            {
                ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                head.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), player.getGameProfile()));
                head.getItem().verifyTagAfterLoad(head.getOrCreateTag());
                EventUtil.dropItem(head, level, player.position());

                hasHeadRunnable.run();
                EventUtil.attackEntity(player, level);
            }
        });
    }

    public static void dropItem(ItemStack stack, Level level, Vec3 pos)
    {
        if (!stack.isEmpty())
        {
            ItemEntity itemEntity = new ItemEntity(level, pos.x, pos.y, pos.z, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    public static void attackEntity(LivingEntity entity, Level level)
    {
        switch (CutterMod.SERVER_CONFIG.cutterDamageType)
        {
            case DIFFICULTY:
                switch (level.getDifficulty())
                {
                    case PEACEFUL:
                        break;
                    case EASY:
                        hurtEntity(entity, level, 2f);
                        break;
                    case NORMAL:
                        hurtEntity(entity, level, 4f);
                        break;
                    case HARD:
                        killEntity(entity);
                        break;
                }
                break;
            case HURT:
                hurtEntity(entity, level, 4f);
                break;
            case KILL:
                killEntity(entity);
                break;
        }
    }

    private static void hurtEntity(LivingEntity entity, Level level, float bias)
    {
        entity.hurt(CutterMisc.BED_CUTTER_DAMAGE, entity.getHealth() / 2f + Mth.nextFloat(level.random, -1f, bias));
    }

    private static void killEntity(LivingEntity entity)
    {
        entity.hurt(CutterMisc.BED_CUTTER_DAMAGE, entity.getHealth());
    }

    public static void addCutterBedsPoi(Stream<Block> blocks, Function<BlockState, PoiType> add)
    {
        blocks.filter(block -> block instanceof CutterBedBlock).flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
                .filter(state -> state.getValue(BedBlock.PART) == BedPart.HEAD).forEach(add::apply);
    }
}
