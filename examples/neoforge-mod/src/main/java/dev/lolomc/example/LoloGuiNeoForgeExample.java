package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@Mod(LoloGuiNeoForgeExample.MOD_ID)
public final class LoloGuiNeoForgeExample {
    static final String MOD_ID = "lolomc_gui_example";
    private static KeyMapping openKey;

    public LoloGuiNeoForgeExample(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::clientSetup);
        context.getModEventBus().addListener(this::registerKeys);
    }

    private void clientSetup(FMLClientSetupEvent event) { }

    private void registerKeys(RegisterKeyMappingsEvent event) {
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.lolomc_gui_example");
        event.register(openKey);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static final class ClientEvents {
        @SubscribeEvent
        public static void tick(ClientTickEvent.Post event) {
            if (openKey == null) return;
            Minecraft client = Minecraft.getInstance();
            while (openKey.consumeClick()) client.setScreen(LoloExampleScreen.create(client));
        }
    }

    static net.minecraft.resources.ResourceLocation resource(String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
