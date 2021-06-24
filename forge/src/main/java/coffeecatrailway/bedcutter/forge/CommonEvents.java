package coffeecatrailway.bedcutter.forge;

import coffeecatrailway.bedcutter.CutterConfig;
import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.common.command.HasHeadCommand;
import coffeecatrailway.bedcutter.forge.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import coffeecatrailway.bedcutter.util.HeadModelUtil;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author CoffeeCatRailway
 * Created: 20/06/2021
 */
@Mod.EventBusSubscriber(modid = CutterMod.MOD_ID)
public class CommonEvents
{
    @SubscribeEvent
    public static void onCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
            event.addCapability(HasHeadCapability.ID, new HasHeadCapability.Provider((Player) event.getObject()));
        if (event.getObject() instanceof Villager)
            event.addCapability(HasHeadCapability.ID, new HasHeadCapability.Provider((Player) event.getObject()));
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(PlayerEvent.PlayerLoggedInEvent event)
    {
        Entity entity = event.getPlayer();
        if (entity instanceof ServerPlayer)
        {
            entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                ServerPlayer serverPlayer = (ServerPlayer) entity;
                CutterNetwork.CHANNEL.sendToPlayer(serverPlayer, new SyncHasHeadMessage(handler.hasHead(), serverPlayer.getId()));
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        if (!event.isWasDeath())
            return;
        Player original = event.getOriginal();
        Player player = event.getPlayer();
        original.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(originalHandler -> player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> handler.setHasHead(originalHandler.hasHead())));
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
        Entity target = event.getTarget();
        Player player = event.getPlayer();

        if (player instanceof ServerPlayer)
            target.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> CutterNetwork.CHANNEL.sendToPlayer((ServerPlayer) player, new SyncHasHeadMessage(handler.hasHead(), target.getId())));
    }

    // tick, wakeup

    @SubscribeEvent
    public static void onLivingRender(RenderLivingEvent.Pre event)
    {
        LivingEntity entity = event.getEntity();
        entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
            EntityModel<?> model = event.getRenderer().getModel();
            HeadModelUtil.setHeadVisible(model, handler.hasHead());
            if (model instanceof HumanoidModel)
                ((HumanoidModel) model).hat.visible = (!(entity instanceof Player) || ((Player) entity).isModelPartShown(PlayerModelPart.HAT)) && handler.hasHead();
        });
    }

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event)
    {
        AutoConfig.getConfigHolder(CutterConfig.class).load();
        CutterMod.LOGGER.info("Server config reloaded");
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event)
    {
        HasHeadCommand.register(event.getDispatcher());
        CutterMod.LOGGER.debug("Has head command registered!");
    }
}
