package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

final class LoloGuiForgeClient {
    private static KeyMapping openKey;

    private LoloGuiForgeClient() { }

    static void init() {
        RegisterKeyMappingsEvent.BUS.addListener(LoloGuiForgeClient::registerKeys);
        TickEvent.ClientTickEvent.Post.BUS.addListener(LoloGuiForgeClient::tick);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        KeyMapping.Category category = KeyMapping.Category.register(
                LoloGuiForgeExample.resource("example"));
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O, category);
        event.register(openKey);
    }

    private static void tick(TickEvent.ClientTickEvent.Post event) {
        if (openKey == null) return;
        Minecraft client = Minecraft.getInstance();
        while (openKey.consumeClick()) client.setScreen(LoloExampleScreen.create(client));
    }
}
