package coffeecatrailway.bedcutter.network;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.network.handler.CutterClientPlayHandler;
import coffeecatrailway.bedcutter.network.handler.CutterServerPlayHandler;
import io.github.ocelot.sonar.common.network.SonarNetworkManager;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * @author CoffeeCatRailway
 * Created: 23/10/2020
 */
public class CutterMessageHandler
{
    public static final String VERSION = "1.0";
    public static final SimpleChannel PLAY = NetworkRegistry.newSimpleChannel(CutterMod.getLocation("play"), () -> VERSION, VERSION::equals, VERSION::equals);

    private static final SonarNetworkManager PLAY_NETWORK_MANAGER = new SonarNetworkManager(PLAY, () -> CutterClientPlayHandler::new, () -> CutterServerPlayHandler::new);

    public static void init()
    {
        PLAY_NETWORK_MANAGER.register(SyncHasHeadMessage.class, SyncHasHeadMessage::new, NetworkDirection.PLAY_TO_CLIENT);
    }
}
