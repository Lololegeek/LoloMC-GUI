package dev.lolomc.example;

import dev.lolomc.gui.GuiSession;
import dev.lolomc.gui.LoloGui;
import dev.lolomc.gui.MinecraftScreenHost;
import dev.lolomc.gui.model.UiDocument;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.resource.Resource;
import net.minecraft.text.Text;
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
        host = new MinecraftScreenHost(session);
    }

    static LoloExampleScreen create(MinecraftClient client) {
        try (InputStream xml = resource(client, LoloGuiExampleClient.resource("ui/screen.xml"));
             InputStream css = resource(client, LoloGuiExampleClient.resource("ui/screen.css"))) {
            UiDocument document = LoloGui.load(xml, text(css));
            GuiSession session = LoloGui.session(document).on("play", node -> { })
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
        return new Scanner(input, StandardCharsets.UTF_8).useDelimiter("\\A").next();
    }

    @Override protected void init() { host.init(width, height); }

    @Override public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
        host.render(new YarnRenderBackend(graphics, textRenderer), mouseX, mouseY);
    }

    @Override public boolean mouseClicked(Click event, boolean doubleClick) {
        return host.mouseClicked(event.x(), event.y(), event.button()) || super.mouseClicked(event, doubleClick);
    }

    @Override public boolean mouseReleased(Click event) {
        return host.mouseReleased() || super.mouseReleased(event);
    }

    @Override public boolean charTyped(CharInput event) {
        int codepoint = event.codepoint();
        if (!Character.isValidCodePoint(codepoint)) return super.charTyped(event);
        boolean handled = false;
        for (char character : Character.toChars(codepoint)) handled |= host.charTyped(character);
        return handled || super.charTyped(event);
    }

    @Override public boolean keyPressed(KeyInput input) {
        return host.keyPressed(input.key()) || super.keyPressed(input);
    }
}
