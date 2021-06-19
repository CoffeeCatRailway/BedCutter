package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.CutterMod;
import net.fabricmc.api.ModInitializer;

public class CutterModFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CutterMod.init();
    }
}
