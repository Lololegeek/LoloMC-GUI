package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.TickEvent.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

@Mod(LoloGuiNeoForgeExample.MOD_ID)
public final class LoloGuiNeoForgeExample {
    static final String MOD_ID = "lolomc_gui_example";
    private static KeyMapping openKey;

    public LoloGuiNeoForgeExample(IEventBus modEventBus) {
        modEventBus.addListener(this::registerKeys);
    }

    private void registerKeys(RegisterKeyMappingsEvent event) {
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O, "category.lolomc_gui_example");
        event.register(openKey);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static final class ClientEvents {
        @SubscribeEvent
        public static void tick(ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END || openKey == null) return;
            Minecraft client = Minecraft.getInstance();
            while (openKey.consumeClick()) client.setScreen(LoloExampleScreen.create(client));
        }
    }

    static ResourceLocation resource(String path) {
        return ResourceLocation.tryParse(MOD_ID + ":" + path);
    }
}
