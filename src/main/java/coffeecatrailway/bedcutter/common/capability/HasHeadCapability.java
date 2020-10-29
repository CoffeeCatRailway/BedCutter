package coffeecatrailway.bedcutter.common.capability;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.network.CutterMessageHandler;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 23/10/2020
 */
public class HasHeadCapability
{
    public static final ResourceLocation ID = CutterMod.getLocation("has_head");
    public static final String TAG_HAS_HEAD = "HasHead";

    @CapabilityInject(IHasHeadHandler.class)
    public static final Capability<IHasHeadHandler> HAS_HEAD_CAP = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IHasHeadHandler.class, new Capability.IStorage<IHasHeadHandler>()
        {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IHasHeadHandler> capability, IHasHeadHandler instance, Direction side)
            {
                CompoundNBT compound = new CompoundNBT();
                compound.putBoolean(TAG_HAS_HEAD, instance.hasHead());
                return compound;
            }

            @Override
            public void readNBT(Capability<IHasHeadHandler> capability, IHasHeadHandler instance, Direction side, INBT nbt)
            {
                instance.setHasHead(((CompoundNBT) nbt).getBoolean(TAG_HAS_HEAD));
            }
        }, HasHeadWrapper::new);
    }

    private static class HasHeadWrapper implements IHasHeadHandler
    {
        boolean hasHead;
        LivingEntity owner;

        public HasHeadWrapper()
        {
            this(null);
        }

        public HasHeadWrapper(final LivingEntity owner)
        {
            this.hasHead = true;
            this.owner = owner;
        }

        @Override
        public boolean hasHead()
        {
            return this.hasHead;
        }

        @Override
        public void setHasHead(boolean hasHead)
        {
            this.hasHead = hasHead;
            if (!this.owner.world.isRemote())
                CutterMessageHandler.PLAY.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.owner), new SyncHasHeadMessage(this.hasHead, this.owner.getEntityId()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static class Provider implements ICapabilitySerializable<INBT>
    {

        final LazyOptional<IHasHeadHandler> optional;
        final IHasHeadHandler handler;

        public Provider(final LivingEntity owner)
        {
            this.handler = new HasHeadWrapper(owner);
            this.optional = LazyOptional.of(() -> this.handler);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return HasHeadCapability.HAS_HEAD_CAP.orEmpty(cap, this.optional);
        }

        @Override
        public INBT serializeNBT()
        {
            return HasHeadCapability.HAS_HEAD_CAP.writeNBT(this.handler, null);
        }

        @Override
        public void deserializeNBT(INBT nbt)
        {
            HasHeadCapability.HAS_HEAD_CAP.readNBT(this.handler, null, nbt);
        }
    }
}
