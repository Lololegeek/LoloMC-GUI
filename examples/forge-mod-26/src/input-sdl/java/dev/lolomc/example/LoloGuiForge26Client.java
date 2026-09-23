package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;

final class LoloGuiForge26Client {
    private static KeyMapping openKey;

    private LoloGuiForge26Client() { }

    public static void init() {
        RegisterKeyMappingsEvent.BUS.addListener(LoloGuiForge26Client::registerKeys);
        TickEvent.ClientTickEvent.Post.BUS.addListener(LoloGuiForge26Client::tick);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(LoloGuiForge26Example.MOD_ID, "example"));
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.KEY_O, category);
        event.register(openKey);
    }

    private static void tick(TickEvent.ClientTickEvent.Post event) {
        if (openKey == null) return;
        Minecraft client = Minecraft.getInstance();
        while (openKey.consumeClick()) client.setScreenAndShow(LoloExampleScreen.create(client));
    }
}
