package dev.lolomc.example;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

@Mod(LoloGuiForgeExample.MOD_ID)
public final class LoloGuiForgeExample {
    static final String MOD_ID = "lolomc_gui_example";
    private static KeyMapping openKey;

    public LoloGuiForgeExample() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeys);
    }

    private void clientSetup(FMLClientSetupEvent event) { }

    private void registerKeys(RegisterKeyMappingsEvent event) {
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.lolomc_gui_example");
        event.register(openKey);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static final class ClientEvents {
        @SubscribeEvent
        public static void tick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END || openKey == null) return;
            Minecraft client = Minecraft.getInstance();
            while (openKey.consumeClick()) client.setScreen(LoloExampleScreen.create(client));
        }
    }

    static net.minecraft.resources.ResourceLocation resource(String path) {
        return new net.minecraft.resources.ResourceLocation(MOD_ID, path);
    }
}
