package coffeecatrailway.bedcutter.client.renderer.entity.layers;

import coffeecatrailway.bedcutter.CommonEvents;
import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import coffeecatrailway.bedcutter.common.capability.IHasHeadHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.VillagerLevelPendantLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHeadToggle;
import net.minecraft.client.resources.data.VillagerMetadataSection;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.IVillagerDataHolder;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

import java.io.IOException;

/**
 * @author CoffeeCatRailway
 * Created: 28/10/2020
 */
@OnlyIn(Dist.CLIENT)
public class ToggleVillagerLevelPendantLayer<T extends LivingEntity & IVillagerDataHolder, M extends EntityModel<T> & IHeadToggle> extends VillagerLevelPendantLayer<T, M> implements IResourceManagerReloadListener
{
    private static final Int2ObjectMap<ResourceLocation> LEVELS = Util.make(new Int2ObjectOpenHashMap<>(), (p_215348_0_) -> {
        p_215348_0_.put(1, new ResourceLocation("stone"));
        p_215348_0_.put(2, new ResourceLocation("iron"));
        p_215348_0_.put(3, new ResourceLocation("gold"));
        p_215348_0_.put(4, new ResourceLocation("emerald"));
        p_215348_0_.put(5, new ResourceLocation("diamond"));
    });
    private final Object2ObjectMap<VillagerType, VillagerMetadataSection.HatType> types = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<VillagerProfession, VillagerMetadataSection.HatType> professions = new Object2ObjectOpenHashMap<>();
    private final IReloadableResourceManager manager;
    private final String type;

    public ToggleVillagerLevelPendantLayer(IEntityRenderer<T, M> renderer, IReloadableResourceManager manager, String type)
    {
        super(renderer, manager, type);
        this.manager = manager;
        this.type = type;
        manager.addReloadListener(this);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if (!entity.isInvisible())
        {
            VillagerData villagerdata = entity.getVillagerData();
            VillagerType villagertype = villagerdata.getType();
            VillagerProfession villagerprofession = villagerdata.getProfession();
            VillagerMetadataSection.HatType villagermetadatasection$hattype = this.func_215350_a(this.types, "type", Registry.VILLAGER_TYPE, villagertype);
            VillagerMetadataSection.HatType villagermetadatasection$hattype1 = this.func_215350_a(this.professions, "profession", Registry.VILLAGER_PROFESSION, villagerprofession);
            M model = this.getEntityModel();

            LazyOptional<IHasHeadHandler> cap = entity.getCapability(HasHeadCapability.HAS_HEAD_CAP);
            boolean hasHead = true;
            if (cap.isPresent())
                hasHead = cap.orElseThrow(NullPointerException::new).hasHead();
            CommonEvents.disableHeadModel(model, hasHead);

            model.func_217146_a(hasHead && (villagermetadatasection$hattype1 == VillagerMetadataSection.HatType.NONE || villagermetadatasection$hattype1 == VillagerMetadataSection.HatType.PARTIAL && villagermetadatasection$hattype != VillagerMetadataSection.HatType.FULL));
            ResourceLocation resourcelocation = this.getTexturePath("type", Registry.VILLAGER_TYPE.getKey(villagertype));
            renderCutoutModel(model, resourcelocation, matrixStack, buffer, packedLight, entity, 1.0F, 1.0F, 1.0F);
            model.func_217146_a(hasHead);
            if (villagerprofession != VillagerProfession.NONE && !entity.isChild())
            {
                ResourceLocation resourcelocation1 = this.getTexturePath("profession", Registry.VILLAGER_PROFESSION.getKey(villagerprofession));
                renderCutoutModel(model, resourcelocation1, matrixStack, buffer, packedLight, entity, 1.0F, 1.0F, 1.0F);
                if (villagerprofession != VillagerProfession.NITWIT)
                {
                    ResourceLocation resourcelocation2 = this.getTexturePath("profession_level", LEVELS.get(MathHelper.clamp(villagerdata.getLevel(), 1, LEVELS.size())));
                    renderCutoutModel(model, resourcelocation2, matrixStack, buffer, packedLight, entity, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

    private ResourceLocation getTexturePath(String path, ResourceLocation name)
    {
        return new ResourceLocation(name.getNamespace(), "textures/entity/" + this.type + "/" + path + "/" + name.getPath() + ".png");
    }

    @Override
    public <K> VillagerMetadataSection.HatType func_215350_a(Object2ObjectMap<K, VillagerMetadataSection.HatType> map, String path, DefaultedRegistry<K> key, K data)
    {
        return map.computeIfAbsent(data, (p_215349_4_) -> {
            try (IResource iresource = this.manager.getResource(this.getTexturePath(path, key.getKey(data))))
            {
                VillagerMetadataSection villagermetadatasection = iresource.getMetadata(VillagerMetadataSection.field_217827_a);
                if (villagermetadatasection != null)
                {
                    return villagermetadatasection.func_217826_a();
                }
            } catch (IOException ioexception)
            {
            }

            return VillagerMetadataSection.HatType.NONE;
        });
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.professions.clear();
        this.types.clear();
    }

    public static boolean isToggle(LayerRenderer<?, ?> layer)
    {
        return layer instanceof VillagerLevelPendantLayer && !(layer instanceof ToggleVillagerLevelPendantLayer);
    }
}
