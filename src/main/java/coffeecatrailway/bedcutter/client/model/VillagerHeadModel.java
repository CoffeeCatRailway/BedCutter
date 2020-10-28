package coffeecatrailway.bedcutter.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * @author CoffeeCatRailway
 * Created: 27/10/2020
 */
public class VillagerHeadModel extends Model
{
    protected ModelRenderer villagerHead;
    protected ModelRenderer hat;
    protected ModelRenderer hatBrim;
    protected final ModelRenderer villagerNose;

    public VillagerHeadModel()
    {
        super(RenderType::getEntityTranslucent);
        int size = 64;

        this.villagerHead = new ModelRenderer(this).setTextureSize(size, size);
        this.villagerHead.setRotationPoint(0f, 0f, 0f);
        this.villagerHead.setTextureOffset(0, 0).addBox(-4f, -10f, -4f, 8f, 10f, 8f, 1f);

        this.hat = new ModelRenderer(this).setTextureSize(size, size);
        this.hat.setRotationPoint(0f, 0f, 0f);
        this.hat.setTextureOffset(32, 0).addBox(-4f, -10f, -4f, 8f, 10f, 8f, 1f + .5f);
        this.villagerHead.addChild(this.hat);

        this.hatBrim = new ModelRenderer(this).setTextureSize(size, size);
        this.hatBrim.setRotationPoint(0f, 0f, 0f);
        this.hatBrim.setTextureOffset(30, 47).addBox(-8f, -8f, -6f, 16f, 16f, 1f, 1f);
        this.hatBrim.rotateAngleX = (-(float) Math.PI / 2f);
        this.hat.addChild(this.hatBrim);

        this.villagerNose = (new ModelRenderer(this)).setTextureSize(size, size);
        this.villagerNose.setRotationPoint(0f, -1.3f, -1f);
        this.villagerNose.setTextureOffset(24, 0).addBox(-1f, -1f, -6f, 2f, 4f, 2f, .25f);
        this.villagerHead.addChild(this.villagerNose);
    }

    public void setRotations(float rotateAngleY)
    {
        this.villagerHead.rotateAngleY = rotateAngleY * ((float) Math.PI / 180f);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        this.villagerHead.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
