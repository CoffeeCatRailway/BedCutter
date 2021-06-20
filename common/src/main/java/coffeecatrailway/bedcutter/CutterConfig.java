package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.CutterMod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

/**
 * @author CoffeeCatRailway
 * Created: 19/06/2021
 */
@Config(name = CutterMod.MOD_ID + "-common")
public class CutterConfig implements ConfigData
{
    //    @Comment("Defines the damage dealt by the bed cutter\nIf 'DIFFICULTY' is selected then peaceful and easy won't hurt you")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public DamageType cutterDamageType = DamageType.DIFFICULTY;

    public enum DamageType
    {
        DIFFICULTY,
        HURT,
        KILL
    }
}
