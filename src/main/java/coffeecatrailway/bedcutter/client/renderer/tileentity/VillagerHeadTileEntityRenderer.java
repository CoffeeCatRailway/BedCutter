package coffeecatrailway.bedcutter.client.renderer.tileentity;

import coffeecatrailway.bedcutter.client.model.VillagerHeadModel;
import coffeecatrailway.bedcutter.common.block.VillagerHeadBlock;
import coffeecatrailway.bedcutter.common.block.WallVillagerHeadBlock;
import coffeecatrailway.bedcutter.common.tileentity.VillagerHeadTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CoffeeCatRailway
 * Created: 26/10/2020
 */
@OnlyIn(Dist.CLIENT)
public class VillagerHeadTileEntityRenderer extends TileEntityRenderer<VillagerHeadTileEntity>
{
    private static final ResourceLocation VILLAGER_TEXTURE = new ResourceLocation("textures/entity/villager/villager.png");
    private static final Map<VillagerType, ResourceLocation> TYPES = getTypes();
    private static final Map<VillagerProfession, ResourceLocation> PROFESSIONS = getProfessions();

    private static final VillagerHeadModel MODEL = new VillagerHeadModel();

    public VillagerHeadTileEntityRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(VillagerHeadTileEntity tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
    {
        BlockState blockstate = tile.getBlockState();
        boolean wallFlag = blockstate.getBlock() instanceof WallVillagerHeadBlock;
        Direction direction = wallFlag ? blockstate.get(WallVillagerHeadBlock.FACING) : null;
        float rotateAngleY = 22.5f * (float) (wallFlag ? (2 + direction.getHorizontalIndex()) * 4 : blockstate.get(VillagerHeadBlock.ROTATION));
        render(direction, rotateAngleY, tile.getVillagerType(), tile.getProfession(), matrixStack, buffer, combinedLight);
    }

    public static void render(@Nullable Direction direction, float rotateAngleY, VillagerType type, VillagerProfession profession, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight)
    {
        matrixStack.push();
        if (direction == null)
            matrixStack.translate(.5f, .05f, .5f);
        else
            matrixStack.translate(.5f - (float) direction.getXOffset() * .25f, .25f, .5f - (float) direction.getZOffset() * .25f);

        matrixStack.scale(-1f, -1f, 1f);
        MODEL.setRotations(rotateAngleY);
        MODEL.render(matrixStack, getRenderType(buffer, VILLAGER_TEXTURE), combinedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        MODEL.render(matrixStack, getRenderType(buffer, TYPES.get(type)), combinedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        if (profession != VillagerProfession.NONE)
            MODEL.render(matrixStack, getRenderType(buffer, PROFESSIONS.get(profession)), combinedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStack.pop();
    }

    private static IVertexBuilder getRenderType(IRenderTypeBuffer buffer, ResourceLocation texture)
    {
        return buffer.getBuffer(RenderType.getEntityTranslucent(texture));
    }

    private static Map<VillagerType, ResourceLocation> getTypes()
    {
        Map<VillagerType, ResourceLocation> types = new HashMap<>();
        Registry.VILLAGER_TYPE.stream()
                .forEach(type -> types.put(type, new ResourceLocation("textures/entity/villager/type/" + type.toString() + ".png")));
        return types;
    }

    private static Map<VillagerProfession, ResourceLocation> getProfessions()
    {
        Map<VillagerProfession, ResourceLocation> professions = new HashMap<>();
        ForgeRegistries.PROFESSIONS.getValues().stream().filter(profession -> "minecraft".equals(profession.getRegistryName().getNamespace()))
                .forEach(profession -> professions.put(profession, new ResourceLocation("textures/entity/villager/profession/" + profession.getRegistryName().getPath() + ".png")));
        return professions;
    }
}
