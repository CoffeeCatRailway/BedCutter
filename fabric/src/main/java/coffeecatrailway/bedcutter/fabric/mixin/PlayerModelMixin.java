package coffeecatrailway.bedcutter.fabric.mixin;

import coffeecatrailway.bedcutter.HasHeadHandler;
import coffeecatrailway.bedcutter.util.HeadModelUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

/**
 * @author CoffeeCatRailway
 * Created: 24/06/2021
 */
@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T>
{
    public PlayerModelMixin(Function function, float f, float g, int i, int j)
    {
        super(function, f, g, i, j);
    }

    @Inject(method = "setupAnim", at = @At("HEAD"))
    public void setupAnim(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        boolean hasHead = HasHeadHandler.hasHead(livingEntity);
        HeadModelUtil.setHeadVisible(this, hasHead);
        this.hat.visible = (!(livingEntity instanceof Player) || ((Player) livingEntity).isModelPartShown(PlayerModelPart.HAT)) && hasHead;
    }
}
