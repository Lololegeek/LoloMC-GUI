package dev.lolomc.gui;

import dev.lolomc.gui.render.RenderBackend;

/** Loader-neutral lifecycle bridge used by version-specific Minecraft Screen classes. */
public final class MinecraftScreenHost {
    private final GuiSession session;

    public MinecraftScreenHost(GuiSession session) { this.session = session; }
    public GuiSession getSession() { return session; }
    public void init(int width, int height) { session.resize(width, height); }

    public void render(RenderBackend backend, double mouseX, double mouseY) {
        session.mouseMoved((float) mouseX, (float) mouseY);
        session.render(backend);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return session.mouseClicked((float) mouseX, (float) mouseY, button);
    }

    public boolean mouseReleased() { session.mouseReleased(); return true; }
    public boolean charTyped(char character) { return session.charTyped(character); }
    public boolean keyPressed(int keyCode) { return session.keyPressed(keyCode); }
}

