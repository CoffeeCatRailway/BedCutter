package coffeecatrailway.bedcutter.fabric.mixin;

import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import coffeecatrailway.bedcutter.util.IHasHead;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author CoffeeCatRailway
 * Created: 21/06/2021
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IHasHead
{
    @Unique
    private static final EntityDataAccessor<Boolean> DATA_HAS_HEAD = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    public LivingEntityMixin(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSynchedData(CallbackInfo ci)
    {
        if (this.isBeheadable())
            this.entityData.define(DATA_HAS_HEAD, true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci)
    {
        if (this.isBeheadable())
            if (compoundTag.contains("hasHead", 1))
                this.setHasHead(compoundTag.getBoolean("hasHead"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci)
    {
        if (this.isBeheadable())
            compoundTag.putBoolean("hasHead", this.hasHead());
    }

    @Override
    public boolean hasHead()
    {
        return this.entityData.get(DATA_HAS_HEAD);
    }

    @Override
    public void setHasHead(boolean hasHead)
    {
        this.entityData.set(DATA_HAS_HEAD, hasHead);
        CutterNetwork.trackingEntityAndSelf(() -> this, new SyncHasHeadMessage(hasHead, this.getId()));
    }

    @Unique
    private boolean isBeheadable()
    {
        return ((Entity) this) instanceof Player || ((Entity) this) instanceof Villager;
    }
}
