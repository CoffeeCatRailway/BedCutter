package coffeecatrailway.bedcutter.util;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;

/**
 * @author CoffeeCatRailway
 * Created: 24/06/2021
 */
public final class HeadModelUtil
{
    public HeadModelUtil()
    {
    }

    public static void setHeadVisible(EntityModel<?> model, boolean visible)
    {
        if (model instanceof HeadedModel)
            ((HeadedModel) model).getHead().visible = visible;
        if (model instanceof HumanoidModel)
        {
            ((HumanoidModel) model).head.visible = visible;
            ((HumanoidModel) model).hat.visible = visible;
        }
    }
}
