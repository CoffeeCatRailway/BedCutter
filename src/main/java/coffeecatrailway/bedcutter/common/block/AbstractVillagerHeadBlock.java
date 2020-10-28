package coffeecatrailway.bedcutter.common.block;

import coffeecatrailway.bedcutter.common.tileentity.VillagerHeadTileEntity;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.enchantment.IArmorVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 28/10/2020
 */
public abstract class AbstractVillagerHeadBlock extends ContainerBlock implements IArmorVanishable
{
    public AbstractVillagerHeadBlock(Properties properties)
    {
        super(properties.hardnessAndResistance(1f));
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
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
            writeType(nbt, head.getVillagerType());
            writeProfession(nbt, head.getProfession());
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
                villagerType = readType(headNbt);
            if (headNbt.contains("profession", Constants.NBT.TAG_STRING))
                profession = readProfession(headNbt);

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
        return Registry.VILLAGER_TYPE.getOptional(getResourceLocationFromNBT(nbt, "type")).orElse(VillagerType.PLAINS);
    }

    public static void writeProfession(CompoundNBT nbt, VillagerProfession profession)
    {
        nbt.putString("profession", profession.getRegistryName().toString());
    }

    public static VillagerProfession readProfession(CompoundNBT nbt)
    {
        VillagerProfession profession = ForgeRegistries.PROFESSIONS.getValue(getResourceLocationFromNBT(nbt, "profession"));
        if (profession == null)
            return VillagerProfession.NONE;
        return profession;
    }

    private static ResourceLocation getResourceLocationFromNBT(CompoundNBT nbt, String key)
    {
        return ResourceLocation.create(nbt.getString(key), ':');
    }
}
