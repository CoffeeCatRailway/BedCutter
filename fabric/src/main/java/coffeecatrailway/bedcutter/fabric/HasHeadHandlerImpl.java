package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.util.IHasHead;
import net.minecraft.world.entity.LivingEntity;

public class HasHeadHandlerImpl
{
    public static boolean hasHead(LivingEntity entity)
    {
        return ((IHasHead) entity).hasHead();
    }

    public static void setHasHead(LivingEntity entity, boolean hasHead)
    {
        ((IHasHead) entity).setHasHead(hasHead);
    }
}
