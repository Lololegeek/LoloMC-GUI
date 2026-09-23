package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

final class LoloGuiNeoForgeClient {
    private static KeyMapping openKey;

    private LoloGuiNeoForgeClient() { }

    static void init(IEventBus modEventBus) {
        modEventBus.addListener(LoloGuiNeoForgeClient::registerKeys);
        NeoForge.EVENT_BUS.addListener(LoloGuiNeoForgeClient::tick);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O, "category.lolomc_gui_example");
        event.register(openKey);
    }

    private static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || openKey == null) return;
        Minecraft client = Minecraft.getInstance();
        while (openKey.consumeClick()) client.setScreen(LoloExampleScreen.create(client));
    }
}
