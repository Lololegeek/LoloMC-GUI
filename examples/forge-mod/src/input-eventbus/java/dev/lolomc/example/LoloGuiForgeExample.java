package dev.lolomc.example;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(LoloGuiForgeExample.MOD_ID)
public final class LoloGuiForgeExample {
    static final String MOD_ID = "lolomc_gui_example";

    public LoloGuiForgeExample() {
        if (FMLEnvironment.dist == Dist.CLIENT) LoloGuiForgeClient.init();
    }

    static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
