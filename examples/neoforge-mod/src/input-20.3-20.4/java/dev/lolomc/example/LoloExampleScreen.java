package dev.lolomc.example;

import dev.lolomc.gui.GuiSession;
import dev.lolomc.gui.LoloGui;
import dev.lolomc.gui.MinecraftScreenHost;
import dev.lolomc.gui.model.UiDocument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;

final class LoloExampleScreen extends Screen {
    private final MinecraftScreenHost host;

    private LoloExampleScreen(GuiSession session) {
        super(Component.empty());
        this.host = new MinecraftScreenHost(session);
    }

    static LoloExampleScreen create(Minecraft client) {
        try {
            InputStream xml = resource(client, LoloGuiNeoForgeExample.resource("ui/screen.xml"));
            String css = text(resource(client, LoloGuiNeoForgeExample.resource("ui/screen.css")));
            UiDocument document = LoloGui.load(xml, css);
            GuiSession session = LoloGui.session(document)
                    .on("play", node -> { })
                    .on("close", node -> client.setScreen(null));
            return new LoloExampleScreen(session);
        } catch (IOException error) {
            throw new IllegalStateException("Unable to load LoloMC GUI example resources", error);
        }
    }

    private static InputStream resource(Minecraft client, ResourceLocation id) throws IOException {
        Optional<Resource> resource = client.getResourceManager().getResource(id);
        if (resource.isEmpty()) throw new IOException("Missing resource " + id);
        return resource.get().open();
    }

    private static String text(InputStream input) {
        return new Scanner(input, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
    }

    @Override protected void init() { host.init(width, height); }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        host.render(new MojmapRenderBackend(graphics, font), mouseX, mouseY);
    }
    @Override public boolean mouseClicked(double x, double y, int button) {
        return host.mouseClicked(x, y, button) || super.mouseClicked(x, y, button);
    }
    @Override public boolean mouseReleased(double x, double y, int button) {
        return host.mouseReleased() || super.mouseReleased(x, y, button);
    }
    @Override public boolean charTyped(char chr, int modifiers) {
        return host.charTyped(chr) || super.charTyped(chr, modifiers);
    }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return host.keyPressed(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
    }
}
