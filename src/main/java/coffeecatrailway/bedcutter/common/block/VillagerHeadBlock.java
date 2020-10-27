package coffeecatrailway.bedcutter.common.block;

import coffeecatrailway.bedcutter.common.tileentity.VillagerHeadTileEntity;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 24/10/2020
 */
public class VillagerHeadBlock extends SkullBlock implements ISortInTab
{
    public VillagerHeadBlock(Properties properties)
    {
        super(Types.VILLAGER, properties);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        ISortInTab.super.fillItemGroup(group, items);
    }

    @Override
    public boolean isType(ItemStack stack)
    {
        return stack.getItem() instanceof SkullItem;
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return new VillagerHeadTileEntity();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        ItemStack stack = new ItemStack(CutterRegistry.VILLAGER_HEAD.get());
        CompoundNBT nbt = stack.getOrCreateTag();

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof VillagerHeadTileEntity)
        {
            VillagerHeadTileEntity head = (VillagerHeadTileEntity) tile;
            VillagerHeadBlock.writeType(nbt, head.getVillagerType());
            VillagerHeadBlock.writeProfession(nbt, head.getProfession());
        }

        return stack;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof VillagerHeadTileEntity)
        {
            VillagerHeadTileEntity villagerHeadTileEntity = (VillagerHeadTileEntity) tileentity;
            VillagerType villagerType = VillagerType.PLAINS;
            VillagerProfession profession = VillagerProfession.NONE;

            CompoundNBT headNbt = stack.getOrCreateTag();
            if (headNbt.contains("type", Constants.NBT.TAG_STRING))
                villagerType = VillagerHeadBlock.readType(headNbt);
            if (headNbt.contains("profession", Constants.NBT.TAG_STRING))
                profession = VillagerHeadBlock.readProfession(headNbt);

            villagerHeadTileEntity.setVillagerType(villagerType);
            villagerHeadTileEntity.setProfession(profession);
        }
    }

    public static void writeType(CompoundNBT nbt, VillagerType type)
    {
        nbt.putString("type", type.toString());
    }

    public static VillagerType readType(CompoundNBT nbt)
    {
        return Registry.VILLAGER_TYPE.getOptional(VillagerHeadBlock.getResourceLocationFromNBT(nbt, "type")).orElse(VillagerType.PLAINS);
    }

    public static void writeProfession(CompoundNBT nbt, VillagerProfession profession)
    {
        nbt.putString("profession", profession.getRegistryName().toString());
    }

    public static VillagerProfession readProfession(CompoundNBT nbt)
    {
        VillagerProfession profession = ForgeRegistries.PROFESSIONS.getValue(VillagerHeadBlock.getResourceLocationFromNBT(nbt, "profession"));
        if (profession == null)
            return VillagerProfession.NONE;
        return profession;
    }

    private static ResourceLocation getResourceLocationFromNBT(CompoundNBT nbt, String key)
    {
        return ResourceLocation.create(nbt.getString(key), ':');
    }

    public enum Types implements SkullBlock.ISkullType
    {
        VILLAGER
    }
}
