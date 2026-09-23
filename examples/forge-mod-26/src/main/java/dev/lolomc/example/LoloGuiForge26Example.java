package dev.lolomc.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(value = LoloGuiForge26Example.MOD_ID, dist = Dist.CLIENT)
public final class LoloGuiForge26Example {
    static final String MOD_ID = "lolomc_gui_example";
    private static KeyMapping openKey;

    public LoloGuiForge26Example(FMLJavaModLoadingContext context) {
        RegisterKeyMappingsEvent.BUS.addListener(this::registerKeys);
        TickEvent.ClientTickEvent.Post.BUS.addListener(LoloGuiForge26Example::tick);
    }

    private void registerKeys(RegisterKeyMappingsEvent event) {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "example"));
        openKey = new KeyMapping("key.lolomc_gui_example.open", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O, category);
        event.register(openKey);
    }

    private static void tick(TickEvent.ClientTickEvent.Post event) {
        if (openKey == null) return;
        Minecraft client = Minecraft.getInstance();
        while (openKey.consumeClick()) client.setScreenAndShow(LoloExampleScreen.create(client));
    }
}
