package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.common.block.AbstractVillagerHeadBlock;
import coffeecatrailway.bedcutter.common.block.CutterBedBlock;
import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.network.CutterMessageHandler;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.SkullPlayerBlock;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.IHeadToggle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author CoffeeCatRailway
 * Created: 27/10/2020
 */
@Mod.EventBusSubscriber(modid = CutterMod.MOD_ID)
public class CommonEvents
{
    @SubscribeEvent
    public static void onCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(HasHeadCapability.ID, new HasHeadCapability.Provider((PlayerEntity) event.getObject()));
        }
        if (event.getObject() instanceof VillagerEntity)
        {
            event.addCapability(HasHeadCapability.ID, new HasHeadCapability.Provider((VillagerEntity) event.getObject()));
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(PlayerEvent.PlayerLoggedInEvent event)
    {
        Entity entity = event.getPlayer();
        if (entity instanceof ServerPlayerEntity)
        {
            entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;
                CutterMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncHasHeadMessage(handler.hasHead(), serverPlayer.getEntityId()));
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        if (!event.isWasDeath())
            return;
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        original.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(originalHandler -> player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> handler.setHasHead(originalHandler.hasHead())));
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
        Entity target = event.getTarget();
        PlayerEntity player = event.getPlayer();

        if (player instanceof ServerPlayerEntity)
        {
            target.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> CutterMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncHasHeadMessage(handler.hasHead(), target.getEntityId())));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        PlayerEntity player = event.player;
        player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
            ItemStack headStack = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
            if (!headStack.isEmpty() && !handler.hasHead())
            {
                if (headStack.getItem() instanceof SkullItem && ((SkullItem) headStack.getItem()).getBlock() instanceof SkullPlayerBlock)
                {
                    GameProfile profile = NBTUtil.readGameProfile(headStack.getOrCreateTag().getCompound("SkullOwner"));
                    if (player.getGameProfile().equals(profile))
                    {
                        handler.setHasHead(true);
                        headStack.shrink(1);
                    }
                }
                player.dropItem(headStack.copy(), false, false);
                headStack.shrink(1);
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event)
    {
        PlayerEntity player = event.getPlayer();
        player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
            if (handler.hasHead())
            {
                player.getBedPosition().ifPresent(pos -> {
                    World world = player.world;
                    if (!(world.getBlockState(pos).getBlock() instanceof CutterBedBlock))
                        return;

                    if (!world.isRemote)
                    {
                        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                        head.getOrCreateTag().put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), player.getGameProfile()));
                        head.getItem().updateItemStackNBT(head.getOrCreateTag());
                        world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), head));

                        handler.setHasHead(false);
                        attackEntity(player, world);
                    }
                });
            }
        });
    }

    @SubscribeEvent
    public static void onLivingRender(RenderLivingEvent.Pre event)
    {
        LivingEntity entity = event.getEntity();
        entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
            EntityModel<?> model = event.getRenderer().getEntityModel();
            disableHeadModel(model, handler.hasHead());
            if (model instanceof BipedModel)
            {
                ((BipedModel) model).bipedHeadwear.showModel = (!(entity instanceof PlayerEntity) || ((PlayerEntity) entity).isWearing(PlayerModelPart.HAT)) && handler.hasHead();
            }
        });
    }

    public static void disableHeadModel(EntityModel<?> model, boolean hasHead)
    {
        if (model instanceof IHeadToggle)
        {
            ((IHeadToggle) model).func_217146_a(hasHead);
        }
        if (model instanceof IHasHead)
        {
            ((IHasHead) model).getModelHead().showModel = hasHead;
        }
        if (model instanceof BipedModel)
        {
            ((BipedModel) model).bipedHead.showModel = hasHead;
            ((BipedModel) model).bipedHeadwear.showModel = hasHead;
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof VillagerEntity)
        {
            VillagerEntity villager = (VillagerEntity) entity;
            World world = villager.world;
            villager.getBrain().getMemory(MemoryModuleType.LAST_SLEPT).ifPresent(lastSleptTime -> {
                long sleptTime = world.getGameTime() - lastSleptTime;
                if (sleptTime >= 0L)
                {
                    villager.getBedPosition().ifPresent(pos -> {
                        if (!(world.getBlockState(pos).getBlock() instanceof CutterBedBlock))
                            return;
                        villager.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                            if (handler.hasHead())
                            {

                                if (!world.isRemote)
                                {
                                    ItemStack head = new ItemStack(CutterRegistry.VILLAGER_HEAD.get());
                                    CompoundNBT nbt = head.getOrCreateTag();
                                    AbstractVillagerHeadBlock.writeType(nbt, villager.getVillagerData().getType());
                                    AbstractVillagerHeadBlock.writeProfession(nbt, villager.getVillagerData().getProfession());
                                    world.addEntity(new ItemEntity(world, villager.getPosX(), villager.getPosY(), villager.getPosZ(), head));

                                    handler.setHasHead(false);
                                    attackEntity(villager, world);
                                }
                            }
                        });
                    });
                }
            });
        }
    }

    // Misc
    private static void attackEntity(LivingEntity entity, World world)
    {
        switch (CutterMod.SERVER_CONFIG.cutterDamageType.get())
        {
            case DIFFICULTY:
                switch (world.getDifficulty())
                {
                    case PEACEFUL:
                        break;
                    case EASY:
                        hurtEntity(entity, world, 2f);
                        break;
                    case NORMAL:
                        hurtEntity(entity, world, 4f);
                        break;
                    case HARD:
                        killEntity(entity);
                        break;
                }
                break;
            case HURT:
                hurtEntity(entity, world, 4f);
                break;
            case KILL:
                killEntity(entity);
                break;
        }
    }

    private static void hurtEntity(LivingEntity entity, World world, float bias)
    {
        entity.attackEntityFrom(CutterRegistry.BED_CUTTER_DAMAGE, entity.getHealth() / 2f + MathHelper.nextFloat(world.rand, -1f, bias));
    }

    private static void killEntity(LivingEntity entity)
    {
        entity.attackEntityFrom(CutterRegistry.BED_CUTTER_DAMAGE, entity.getHealth());
    }
}
