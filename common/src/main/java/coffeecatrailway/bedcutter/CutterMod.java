package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import coffeecatrailway.bedcutter.registry.CutterBlocks;
import com.mojang.authlib.GameProfile;
import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.architectury.event.events.TickEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CutterMod
{
    public static final String MOD_ID = "bedcutter";
    public static final Logger LOGGER = getLogger("");

    public static CutterConfig SERVER_CONFIG;

    public static void init()
    {
        SERVER_CONFIG = AutoConfig.register(CutterConfig.class, Toml4jConfigSerializer::new).getConfig();
        CutterMod.LOGGER.info("Server config registered");

        CutterNetwork.init();
        CutterBlocks.load();
        CutterMisc.load();

        EntityEvent.ADD.register((entity, level) -> {
            if (entity instanceof ServerPlayer)
            {
                CutterNetwork.CHANNEL.sendToPlayer((ServerPlayer) entity, new SyncHasHeadMessage(HasHeadHandler.hasHead((ServerPlayer) entity), entity.getId()));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> CutterNetwork.CHANNEL.sendToPlayer(serverPlayer, new SyncHasHeadMessage(HasHeadHandler.hasHead(serverPlayer), serverPlayer.getId())));
        PlayerEvent.PLAYER_CLONE.register((original, serverPlayer, wasDeath) -> {
            if (!wasDeath)
                return;
            HasHeadHandler.setHasHead(serverPlayer, HasHeadHandler.hasHead(original));
        });
        // TODO: Find alternate to PlayerEvent.StartTracking
        TickEvent.PLAYER_POST.register(player -> {
            if (player.level.isClientSide())
                return;
            ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
            if (!headStack.isEmpty() && !HasHeadHandler.hasHead(player))
            {
                if (headStack.getItem() instanceof PlayerHeadItem && ((PlayerHeadItem) headStack.getItem()).getBlock() instanceof PlayerHeadBlock)
                {
                    GameProfile profile = NbtUtils.readGameProfile(headStack.getOrCreateTag().getCompound("SkullOwner"));
                    if (player.getGameProfile().equals(profile))
                    {
                        HasHeadHandler.setHasHead(player, true);
                        headStack.shrink(1);
                    }
                }
                if (!headStack.isEmpty())
                {
                    dropItem(headStack.copy(), player.level, player.position());
                    headStack.shrink(1);
                }
            }
        });
    }

    private static void dropItem(ItemStack stack, Level level, Vec3 pos)
    {
        if (!stack.isEmpty())
        {
            ItemEntity itemEntity = new ItemEntity(level, pos.x, pos.y, pos.z, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(MOD_ID + (!StringUtil.isNullOrEmpty(name) ? "-" + name : ""));
    }
}
