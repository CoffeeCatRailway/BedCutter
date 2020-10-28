package coffeecatrailway.bedcutter.client.renderer.tileentity.itemstack;

import coffeecatrailway.bedcutter.client.renderer.tileentity.VillagerHeadTileEntityRenderer;
import coffeecatrailway.bedcutter.common.block.AbstractVillagerHeadBlock;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author CoffeeCatRailway
 * Created: 26/10/2020
 */
public class VillagerHeadItemStackTERenderer extends ItemStackTileEntityRenderer
{
    public static final VillagerHeadItemStackTERenderer INSTANCE = new VillagerHeadItemStackTERenderer();

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
    {
        matrixStack.push();
        CompoundNBT nbt = stack.getOrCreateTag();
        matrixStack.scale(.75f, .75f, .75f);
        matrixStack.translate(.175f, 0f, .175f);
        VillagerHeadTileEntityRenderer.render(null, 180f, AbstractVillagerHeadBlock.readType(nbt), AbstractVillagerHeadBlock.readProfession(nbt), matrixStack, buffer, combinedLight);
        matrixStack.pop();
    }
}
