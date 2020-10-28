package coffeecatrailway.bedcutter.network.handler;

import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 23/10/2020
 */
public class CutterClientPlayHandler implements ICutterClientPlayHandler
{
    @Override
    public void handleSyncHasCutterMessage(SyncHasHeadMessage message, NetworkEvent.Context ctx)
    {
        ctx.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(message.getOwner());
            entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                handler.setHasHead(message.hasHead());
            });
        });
        ctx.setPacketHandled(true);
    }
}
