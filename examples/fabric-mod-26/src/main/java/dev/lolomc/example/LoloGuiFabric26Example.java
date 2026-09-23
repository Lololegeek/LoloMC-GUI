package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class LoloGuiFabric26Example implements ClientModInitializer {
    private static final String MOD_ID = "lolomc_gui_example";
    private KeyMapping openKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "example"));
        openKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.lolomc_gui_example.open", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.consumeClick()) client.gui.setScreen(LoloExampleScreen.create(client));
        });
    }
}
