package coffeecatrailway.bedcutter.forge;

import coffeecatrailway.bedcutter.forge.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.util.IHasHead;
import net.minecraft.world.entity.LivingEntity;

public class HasHeadHandlerImpl
{
    public static boolean hasHead(LivingEntity entity)
    {
        IHasHead handler = entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).orElse(null);
        return handler != null && handler.hasHead();
    }

    public static void setHasHead(LivingEntity entity, boolean hasHead)
    {
        entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> handler.setHasHead(hasHead));
    }
}
