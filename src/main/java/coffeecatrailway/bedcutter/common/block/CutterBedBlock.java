package coffeecatrailway.bedcutter.common.block;

import coffeecatrailway.bedcutter.CutterMod;
import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.registry.CutterRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

/**
 * @author CoffeeCatRailway
 * Created: 21/10/2020
 */
@Mod.EventBusSubscriber(modid = CutterMod.MOD_ID)
public class CutterBedBlock extends BedBlock
{
    public CutterBedBlock(Properties properties)
    {
        super(DyeColor.WHITE, properties);
    }

    /**
     * Based on: https://github.com/Ocelot5836/Sonar/blob/1.16.3/src/main/java/io/github/ocelot/sonar/common/item/SpawnEggItemBase.java#L48
     */
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (group == ItemGroup.DECORATIONS)
        {
            if (items.stream().anyMatch(stack -> stack.getItem() instanceof BedItem))
            {
                Optional<ItemStack> optional = items.stream().filter(stack -> stack.getItem() instanceof BedItem).reduce((a, b) -> b);
                if (optional.isPresent() && items.contains(optional.get()))
                {
                    items.add(items.indexOf(optional.get()) + 1, new ItemStack(this));
                    return;
                }
            }
            items.add(new ItemStack(this));
        }
    }

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event)
    {
        event.getPlayer().getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
            PlayerModel<?> model = event.getRenderer().getEntityModel();
            model.bipedHead.showModel = handler.hasHead();
            model.bipedDeadmau5Head.showModel = handler.hasHead();
        });
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event)
    {
        PlayerEntity player = event.getPlayer();
        player.getBedPosition().ifPresent(pos -> {
            World world = player.world;
            if (!(world.getBlockState(pos).getBlock() instanceof CutterBedBlock))
                return;

            if (!world.isRemote)
            {
                ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                head.getOrCreateTag().put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), player.getGameProfile()));
                head.getItem().updateItemStackNBT(head.getOrCreateTag());
                world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), head));

                player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> handler.setHasHead(false));
            }
        });
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return null;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
