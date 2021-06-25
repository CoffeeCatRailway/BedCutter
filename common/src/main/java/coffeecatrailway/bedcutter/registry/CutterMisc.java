package coffeecatrailway.bedcutter.registry;

import coffeecatrailway.bedcutter.CutterMod;
import net.minecraft.world.damagesource.DamageSource;
import org.apache.logging.log4j.Logger;

/**
 * @author CoffeeCatRailway
 * Created: 25/06/2021
 */
public class CutterMisc
{
    private static final Logger LOGGER = CutterMod.getLogger("Misc");

    public static final DamageSource BED_CUTTER_DAMAGE = new CutterDamageSource("beb_cutter");

    public static void load()
    {
        LOGGER.debug("Loaded");
    }

    public static class CutterDamageSource extends DamageSource
    {
        public CutterDamageSource(String string)
        {
            super(string);
            this.bypassArmor();
            this.bypassInvul();
            this.bypassMagic();
        }
    }
}
