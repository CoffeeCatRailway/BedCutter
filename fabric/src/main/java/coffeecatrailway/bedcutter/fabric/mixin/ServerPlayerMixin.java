package coffeecatrailway.bedcutter.fabric.mixin;

import coffeecatrailway.bedcutter.HasHeadHandler;
import coffeecatrailway.bedcutter.util.EventUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author CoffeeCatRailway
 * Created: 25/06/2021
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player
{
    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile)
    {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "stopSleepInBed", at = @At("HEAD"))
    public void stopSleepInBed(boolean wakeImmediately, boolean updateWorldFlag, CallbackInfo ci)
    {
        if (HasHeadHandler.hasHead(this))
            EventUtil.playerWakeUp(this, this.level, () -> HasHeadHandler.setHasHead(this, false));
    }
}
