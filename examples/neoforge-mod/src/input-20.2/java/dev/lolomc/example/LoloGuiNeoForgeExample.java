package dev.lolomc.example;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(LoloGuiNeoForgeExample.MOD_ID)
public final class LoloGuiNeoForgeExample {
    static final String MOD_ID = "lolomc_gui_example";

    public LoloGuiNeoForgeExample(IEventBus modEventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) LoloGuiNeoForgeClient.init(modEventBus);
    }

    static ResourceLocation resource(String path) {
        return ResourceLocation.tryParse(MOD_ID + ":" + path);
    }
}
