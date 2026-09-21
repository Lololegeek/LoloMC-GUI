package dev.lolomc.example;

import dev.lolomc.gui.render.RenderBackend;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

final class YarnRenderBackend implements RenderBackend {
    private final DrawContext graphics;
    private final TextRenderer font;

    YarnRenderBackend(DrawContext graphics, TextRenderer font) {
        this.graphics = graphics;
        this.font = font;
    }

    @Override public void fill(float x, float y, float width, float height, int argb, float radius) {
        graphics.fill((int) x, (int) y, (int) (x + width), (int) (y + height), argb);
    }

    @Override public void text(String text, float x, float y, int argb, float size, String align) {
        float scale = size / 9.0f;
        graphics.getMatrices().push();
        graphics.getMatrices().scale(scale, scale, 1.0f);
        graphics.drawText(font, text, (int) (x / scale), (int) (y / scale), argb, false);
        graphics.getMatrices().pop();
    }

    @Override public void image(String resource, float x, float y, float width, float height, int tint) {
        // Add a ResourceLocation + blit call here for image nodes used by your mod.
    }

    @Override public void pushClip(float x, float y, float width, float height) {
        graphics.enableScissor((int) x, (int) y, (int) (x + width), (int) (y + height));
    }

    @Override public void popClip() { graphics.disableScissor(); }
}
