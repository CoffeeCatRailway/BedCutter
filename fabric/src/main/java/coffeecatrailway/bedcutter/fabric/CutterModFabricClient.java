package coffeecatrailway.bedcutter.fabric;

import coffeecatrailway.bedcutter.registry.CutterBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 21/06/2021
 */
@Environment(EnvType.CLIENT)
public class CutterModFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        CutterBlocks.BED_CUTTERS.stream().map(Supplier::get).forEach(bed -> BlockRenderLayerMap.INSTANCE.putBlock(bed, RenderType.cutoutMipped()));
    }
}
