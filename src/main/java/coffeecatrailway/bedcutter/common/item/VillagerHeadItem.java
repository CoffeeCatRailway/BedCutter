package coffeecatrailway.bedcutter.common.item;

import coffeecatrailway.bedcutter.common.block.VillagerHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 27/10/2020
 */
public class VillagerHeadItem extends WallOrFloorItem
{
    public VillagerHeadItem(Block floorBlock, Block wallBlock, Properties properties)
    {
        super(floorBlock, wallBlock, properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        CompoundNBT stackNbt = stack.getOrCreateTag();

        if (!stackNbt.contains("type", Constants.NBT.TAG_STRING))
            VillagerHeadBlock.writeType(stackNbt, VillagerType.PLAINS);

        if (!stackNbt.contains("profession", Constants.NBT.TAG_STRING))
            VillagerHeadBlock.writeProfession(stackNbt, VillagerProfession.NONE);
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public boolean updateItemStackNBT(CompoundNBT nbt)
    {
        int i = 0;
        super.updateItemStackNBT(nbt);
        if (nbt.contains("type", Constants.NBT.TAG_STRING) && !StringUtils.isBlank(nbt.getString("type")))
        {
            VillagerHeadBlock.writeType(nbt, VillagerHeadBlock.readType(nbt));
            i++;
        }
        if (nbt.contains("profession", Constants.NBT.TAG_STRING) && !StringUtils.isBlank(nbt.getString("profession")))
        {
            VillagerHeadBlock.writeProfession(nbt, VillagerHeadBlock.readProfession(nbt));
            i++;
        }

        return i != 0;
    }
}
