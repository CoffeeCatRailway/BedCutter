package coffeecatrailway.bedcutter;

import coffeecatrailway.bedcutter.registry.CutterRegistry;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CutterMod.MOD_ID)
public class CutterMod
{
    public static final String MOD_ID = "bedcutter";
    private static final Logger LOGGER = getLogger("");

    public static Registrate REGISTRATE;

    public CutterMod()
    {
        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> ItemGroup.DECORATIONS)
                .addDataGenerator(ProviderType.BLOCK_TAGS, new CutterTags.Blocks())
                .addDataGenerator(ProviderType.ITEM_TAGS, new CutterTags.Items());

        MinecraftForge.EVENT_BUS.register(this);

        CutterRegistry.load();
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(CutterMod.MOD_ID, path);
    }

    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(CutterMod.MOD_ID + "-" + name);
    }
}
