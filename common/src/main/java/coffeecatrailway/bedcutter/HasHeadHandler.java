package coffeecatrailway.bedcutter;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;

public class HasHeadHandler
{
    @ExpectPlatform
    public static boolean hasHead(LivingEntity entity)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setHasHead(LivingEntity entity, boolean hasHead)
    {
        throw new AssertionError();
    }
}
