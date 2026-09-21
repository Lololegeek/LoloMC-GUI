package dev.lolomc.example;

import dev.lolomc.gui.render.RenderBackend;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

final class MojmapRenderBackend implements RenderBackend {
    private final GuiGraphics graphics;
    private final Font font;

    MojmapRenderBackend(GuiGraphics graphics, Font font) { this.graphics = graphics; this.font = font; }

    @Override public void fill(float x, float y, float width, float height, int argb, float radius) {
        graphics.fill((int) x, (int) y, (int) (x + width), (int) (y + height), argb);
    }

    @Override public void text(String text, float x, float y, int argb, float size, String align) {
        float scale = size / 9.0f;
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0f);
        graphics.drawString(font, text, (int) (x / scale), (int) (y / scale), argb, false);
        graphics.pose().popPose();
    }

    @Override public void pushClip(float x, float y, float width, float height) { graphics.enableScissor((int) x, (int) y, (int) (x + width), (int) (y + height)); }
    @Override public void popClip() { graphics.disableScissor(); }
}
