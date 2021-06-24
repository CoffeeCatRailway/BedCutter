package coffeecatrailway.bedcutter.network;

import coffeecatrailway.bedcutter.HasHeadHandler;
import me.shedaniel.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 23/06/2021
 */
public class SyncHasHeadMessage
{
    private boolean hasHead;
    private int owner;

    public SyncHasHeadMessage(boolean hasHead, int owner)
    {
        this.hasHead = hasHead;
        this.owner = owner;
    }

    public static void encode(SyncHasHeadMessage message, FriendlyByteBuf buf)
    {
        buf.writeBoolean(message.hasHead);
        buf.writeVarInt(message.owner);
    }

    public static SyncHasHeadMessage decode(FriendlyByteBuf buf)
    {
        return new SyncHasHeadMessage(buf.readBoolean(), buf.readVarInt());
    }

    public static void handle(SyncHasHeadMessage message, Supplier<NetworkManager.PacketContext> contextSupplier)
    {
        NetworkManager.PacketContext context = contextSupplier.get();
        context.queue(() -> {
            if (context.getEnv() == EnvType.CLIENT)
            {
                Entity entity = Minecraft.getInstance().level.getEntity(message.owner);
                if (entity instanceof LivingEntity)
                    HasHeadHandler.setHasHead((LivingEntity) entity, message.hasHead);
            }
        });
    }
}
