package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@Mod(LoloGuiNeoForgeExample.MOD_ID)
public final class LoloGuiNeoForgeExample {
    static final String MOD_ID = "lolomc_gui_example";
    private static KeyMapping openKey;

    public LoloGuiNeoForgeExample(IEventBus modEventBus) { modEventBus.addListener(this::registerKeys); }

    private void registerKeys(RegisterKeyMappingsEvent event) {
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.withNamespaceAndPath(MOD_ID, "example"));
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O, category);
        event.register(openKey);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static final class ClientEvents {
        @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
            if (openKey == null) return;
            Minecraft client = Minecraft.getInstance();
            while (openKey.consumeClick()) client.setScreen(LoloExampleScreen.create(client));
        }
    }

    static Identifier resource(String path) { return Identifier.withNamespaceAndPath(MOD_ID, path); }
}
