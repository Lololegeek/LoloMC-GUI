package dev.lolomc.example;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(LoloGuiForge26Example.MOD_ID)
public final class LoloGuiForge26Example {
    static final String MOD_ID = "lolomc_gui_example";

    public LoloGuiForge26Example(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist == Dist.CLIENT) LoloGuiForge26Client.init();
    }
}
