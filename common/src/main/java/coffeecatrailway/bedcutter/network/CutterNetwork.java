package coffeecatrailway.bedcutter.network;

import coffeecatrailway.bedcutter.CutterMod;
import me.shedaniel.architectury.networking.NetworkChannel;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 19/06/2021
 */
public class CutterNetwork
{
    private static final String VERSION = "1.0";
    public static final NetworkChannel CHANNEL = NetworkChannel.create(CutterMod.getLocation("network"));

    public static void init()
    {
        CHANNEL.register(SyncHasHeadMessage.class, SyncHasHeadMessage::encode, SyncHasHeadMessage::decode, SyncHasHeadMessage::handle);
    }

    public static <T> void trackingEntityAndSelf(final Supplier<Entity> entitySupplier, T message)
    {
        final Entity entity = entitySupplier.get();
        if (entity.getCommandSenderWorld().getChunkSource() instanceof ServerChunkCache)
            ((ServerChunkCache) entity.getCommandSenderWorld().getChunkSource()).broadcastAndSend(entity, CHANNEL.toPacket(NetworkManager.serverToClient(), message));
    }
}
