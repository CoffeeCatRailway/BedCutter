package coffeecatrailway.bedcutter.network;

import coffeecatrailway.bedcutter.CutterMod;
import me.shedaniel.architectury.networking.NetworkChannel;

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
    }
}
