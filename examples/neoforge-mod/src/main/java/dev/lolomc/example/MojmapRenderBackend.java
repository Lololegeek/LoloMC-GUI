package dev.lolomc.example;

import dev.lolomc.gui.render.RenderBackend;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

final class MojmapRenderBackend implements RenderBackend {
    private final GuiGraphics graphics;
    private final Font font;

    MojmapRenderBackend(GuiGraphics graphics, Font font) { this.graphics = graphics; this.font = font; }
    @Override public void fill(float x, float y, float width, float height, int argb, float radius) { graphics.fill((int) x, (int) y, (int) (x + width), (int) (y + height), argb); }
    @Override public void text(String text, float x, float y, int argb, float size, String align) {
        float scale = size / 9.0f;
        MatrixTransformCompat.pushScale(graphics.pose(), scale);
        graphics.drawString(font, text, (int) (x / scale), (int) (y / scale), argb, false);
        MatrixTransformCompat.pop(graphics.pose());
    }
    @Override public void image(String resource, float x, float y, float width, float height, int tint) {
        // Resource-backed image drawing is intentionally left to the host mod.
        // The runtime still exposes the node so a mod can map it to its atlas.
    }
    @Override public void pushClip(float x, float y, float width, float height) { graphics.enableScissor((int) x, (int) y, (int) (x + width), (int) (y + height)); }
    @Override public void popClip() { graphics.disableScissor(); }
}
