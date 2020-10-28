package coffeecatrailway.bedcutter.common.tileentity;

import coffeecatrailway.bedcutter.common.block.AbstractVillagerHeadBlock;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 24/10/2020
 */
public class VillagerHeadTileEntity extends TileEntity
{
    private VillagerType villagerType;
    private VillagerProfession profession;

    public VillagerHeadTileEntity()
    {
        this(CutterRegistry.VILLAGER_HEAD_TILE.get());
    }

    public VillagerHeadTileEntity(TileEntityType<VillagerHeadTileEntity> type)
    {
        super(type);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        if (this.villagerType != null)
            AbstractVillagerHeadBlock.writeType(nbt, this.villagerType);
        if (this.profession != null)
            AbstractVillagerHeadBlock.writeProfession(nbt, this.profession);
        return nbt;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        if (nbt.contains("type", Constants.NBT.TAG_STRING))
            this.setVillagerType(AbstractVillagerHeadBlock.readType(nbt));
        if (nbt.contains("profession", Constants.NBT.TAG_STRING))
            this.setProfession(AbstractVillagerHeadBlock.readProfession(nbt));
    }

    @OnlyIn(Dist.CLIENT)
    public VillagerType getVillagerType()
    {
        return this.villagerType;
    }

    public void setVillagerType(VillagerType villagerType)
    {
        this.villagerType = villagerType;
        this.markDirty();
    }

    @OnlyIn(Dist.CLIENT)
    public VillagerProfession getProfession()
    {
        return this.profession;
    }

    public void setProfession(VillagerProfession profession)
    {
        this.profession = profession;
        this.markDirty();
    }
}
