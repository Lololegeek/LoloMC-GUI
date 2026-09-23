package dev.lolomc.example;

import dev.lolomc.gui.GuiSession;
import dev.lolomc.gui.LoloGui;
import dev.lolomc.gui.MinecraftScreenHost;
import dev.lolomc.gui.model.UiDocument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

final class LoloExampleScreen extends Screen {
    private final MinecraftScreenHost host;

    private LoloExampleScreen(GuiSession session) {
        super(Component.empty());
        host = new MinecraftScreenHost(session);
    }

    static LoloExampleScreen create(Minecraft client) {
        try (InputStream xml = resource("screen.xml"); InputStream css = resource("screen.css")) {
            UiDocument document = LoloGui.load(xml, text(css));
            GuiSession session = LoloGui.session(document)
                    .on("play", node -> {
                        if (client.player != null) client.player.sendSystemMessage(Component.literal("LoloMC GUI: play"));
                    })
                    .on("close", node -> client.setScreen(null));
            return new LoloExampleScreen(session);
        } catch (IOException error) {
            throw new IllegalStateException("Unable to load LoloMC GUI example resources", error);
        }
    }

    private static InputStream resource(String name) throws IOException {
        InputStream stream = LoloExampleScreen.class.getResourceAsStream(
                "/assets/lolomc_gui_example/ui/" + name);
        if (stream == null) throw new IOException("Missing example UI resource " + name);
        return stream;
    }

    private static String text(InputStream input) {
        return new Scanner(input, StandardCharsets.UTF_8).useDelimiter("\\A").next();
    }

    @Override
    protected void init() {
        host.init(width, height);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        host.render(new Mojmap26RenderBackend(graphics, font), mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return host.mouseClicked(event.x(), event.y(), event.button()) || super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        return host.mouseReleased() || super.mouseReleased(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        int codepoint = event.codepoint();
        if (!Character.isValidCodePoint(codepoint)) return super.charTyped(event);
        boolean handled = false;
        for (char character : Character.toChars(codepoint)) handled |= host.charTyped(character);
        return handled || super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return host.keyPressed(event.key()) || super.keyPressed(event);
    }
}
