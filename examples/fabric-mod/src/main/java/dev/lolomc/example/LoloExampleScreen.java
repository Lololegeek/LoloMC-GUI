package dev.lolomc.example;

import dev.lolomc.gui.GuiSession;
import dev.lolomc.gui.LoloGui;
import dev.lolomc.gui.MinecraftScreenHost;
import dev.lolomc.gui.model.UiDocument;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;

final class LoloExampleScreen extends Screen {
    private final MinecraftScreenHost host;

    private LoloExampleScreen(GuiSession session) {
        super(Text.empty());
        this.host = new MinecraftScreenHost(session);
    }

    static LoloExampleScreen create(MinecraftClient client) {
        try {
            InputStream xml = resource(client, LoloGuiExampleClient.resource("ui/screen.xml"));
            String css = text(resource(client, LoloGuiExampleClient.resource("ui/screen.css")));
            UiDocument document = LoloGui.load(xml, css);
            GuiSession session = LoloGui.session(document)
                    .on("play", node -> { if (client.player != null) client.player.sendMessage(Text.literal("LoloMC GUI: play"), false); })
                    .on("nameChanged", node -> { })
                    .on("close", node -> client.setScreen(null));
            return new LoloExampleScreen(session);
        } catch (IOException error) {
            throw new IllegalStateException("Unable to load LoloMC GUI example resources", error);
        }
    }

    private static InputStream resource(MinecraftClient client, Identifier id) throws IOException {
        Optional<Resource> resource = client.getResourceManager().getResource(id);
        if (resource.isEmpty()) throw new IOException("Missing resource " + id);
        return resource.get().getInputStream();
    }

    private static String text(InputStream input) {
        return new Scanner(input, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
    }

    @Override protected void init() { host.init(width, height); }

    @Override public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
        host.render(new YarnRenderBackend(graphics, textRenderer), mouseX, mouseY);
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
