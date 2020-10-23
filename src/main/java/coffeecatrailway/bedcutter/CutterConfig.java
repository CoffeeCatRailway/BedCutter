package coffeecatrailway.bedcutter;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author CoffeeCatRailway
 * Created: 22/10/2020
 */
public class CutterConfig
{
    public ForgeConfigSpec.EnumValue<CutterDamageType> cutterDamageType;

    public CutterConfig(ForgeConfigSpec.Builder builder)
    {
        builder.comment("Server Configurable Settings").push("block");
        cutterDamageType = builder.comment("Defines the damage dealt by the bed cutter", "If 'DIFFICULTY' is selected then peaceful and easy won't hurt you")
                .translation("config." + CutterMod.MOD_ID + ".block.cutterDamageType").defineEnum("cutter_damage_type", CutterDamageType.DIFFICULTY, CutterDamageType.values());
    }

    public enum CutterDamageType
    {
        DIFFICULTY,
        HURT,
        KILL
    }
}
