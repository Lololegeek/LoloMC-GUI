package dev.lolomc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class LoloGuiExampleClient implements ClientModInitializer {
    private static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("lolomc_gui_example", "example"));
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lolomc_gui_example.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) client.setScreen(LoloExampleScreen.create(client));
        });
    }

    static Identifier resource(String path) {
        return Identifier.of("lolomc_gui_example", path);
    }
}
