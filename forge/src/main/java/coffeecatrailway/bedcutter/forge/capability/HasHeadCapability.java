package coffeecatrailway.bedcutter.forge.capability;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.network.CutterNetwork;
import coffeecatrailway.bedcutter.network.SyncHasHeadMessage;
import coffeecatrailway.bedcutter.util.IHasHead;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * @author CoffeeCatRailway
 * Created: 22/06/2021
 */
public class HasHeadCapability
{
    public static final ResourceLocation ID = CutterMod.getLocation("has_head");

    @CapabilityInject(IHasHead.class)
    public static final Capability<IHasHead> HAS_HEAD_CAP = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IHasHead.class, new Capability.IStorage<IHasHead>()
        {
            @Nullable
            @Override
            public Tag writeNBT(Capability<IHasHead> capability, IHasHead instance, Direction side)
            {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("hasHead", instance.hasHead());
                return tag;
            }

            @Override
            public void readNBT(Capability<IHasHead> capability, IHasHead instance, Direction side, Tag tag)
            {
                instance.setHasHead(((CompoundTag) tag).getBoolean("hasHead"));
            }
        }, HasHeadWrapper::new);
    }

    private static class HasHeadWrapper implements IHasHead
    {
        private boolean hasHead;
        private LivingEntity owner;

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
            CutterNetwork.trackingEntityAndSelf(() -> this.owner, new SyncHasHeadMessage(this.hasHead, this.owner.getId()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static class Provider implements ICapabilitySerializable<Tag>
    {
        private final IHasHead handler;
        private final LazyOptional<IHasHead> optional;

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
        public Tag serializeNBT()
        {
            return HasHeadCapability.HAS_HEAD_CAP.writeNBT(this.handler, null);
        }

        @Override
        public void deserializeNBT(Tag tag)
        {
            HasHeadCapability.HAS_HEAD_CAP.readNBT(this.handler, null, tag);
        }
    }
}
