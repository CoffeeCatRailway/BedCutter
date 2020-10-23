package coffeecatrailway.bedcutter.network.handler;

import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 23/10/2020
 */
public interface ICutterClientPlayHandler
{
    void handleSyncHasCutterMessage(SyncHasHeadMessage message, NetworkEvent.Context ctx);
}
