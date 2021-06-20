package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.CutterConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

/**
 * @author CoffeeCatRailway
 * Created: 19/06/2021
 */
@Environment(EnvType.CLIENT)
public class CutterModMenu implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory()
    {
        return screen -> AutoConfig.getConfigScreen(CutterConfig.class, screen).get();
    }
}
