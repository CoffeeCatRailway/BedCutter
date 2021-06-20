package coffeecatrailway.bedcutter.network;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.config.CutterConfig;
import me.shedaniel.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 19/06/2021
 */
public class SyncConfigMessage
{
    private final byte[] configData;
    private static final ByteConfigSerializer<CutterConfig> serializer = new ByteConfigSerializer<>();

    public SyncConfigMessage(CutterConfig config)
    {
        this(serializer.serialize(config));
    }

    public SyncConfigMessage(byte[] configData)
    {
        this.configData = configData;
    }

    public static void encode(SyncConfigMessage message, FriendlyByteBuf buf)
    {
        buf.writeByteArray(message.configData);
    }

    public static SyncConfigMessage decode(FriendlyByteBuf buf)
    {
        return new SyncConfigMessage(buf.readByteArray());
    }

    public static void handle(SyncConfigMessage message, Supplier<NetworkManager.PacketContext> contextSupplier)
    {
        NetworkManager.PacketContext context = contextSupplier.get();
        context.queue(() -> {
            if (context.getEnv() == EnvType.CLIENT)
                serializer.deserialize(message.configData).ifPresent(CutterMod::setSyncedServerConfig);
        });
    }
}
