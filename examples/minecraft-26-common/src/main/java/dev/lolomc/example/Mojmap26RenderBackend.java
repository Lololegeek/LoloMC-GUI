package dev.lolomc.example;

import dev.lolomc.gui.render.RenderBackend;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Rendering adapter for Minecraft's extracted GUI render-state pipeline. */
final class Mojmap26RenderBackend implements RenderBackend {
    private final GuiGraphicsExtractor graphics;
    private final Font font;

    Mojmap26RenderBackend(GuiGraphicsExtractor graphics, Font font) {
        this.graphics = graphics;
        this.font = font;
    }

    @Override
    public void fill(float x, float y, float width, float height, int argb, float radius) {
        if (width <= 0 || height <= 0) return;
        if (radius <= 0.5f) {
            graphics.fill((int) x, (int) y, (int) (x + width), (int) (y + height), argb);
            return;
        }
        int rows = Math.max(1, (int) Math.ceil(height));
        for (int row = 0; row < rows; row++) {
            float inset = roundedInset(width, height, radius, row + 0.5f);
            graphics.fill((int) (x + inset), (int) (y + row), (int) (x + width - inset), (int) (y + row + 1), argb);
        }
    }

    @Override
    public void gradient(float x, float y, float width, float height, int startArgb, int endArgb,
                         boolean vertical, float radius) {
        int bands = Math.max(1, (int) Math.ceil(vertical ? height : width));
        for (int band = 0; band < bands; band++) {
            float progress = bands == 1 ? 0 : band / (float) (bands - 1);
            int color = mix(startArgb, endArgb, progress);
            if (vertical) fill(x, y + height * progress, width, height / bands + 1, color, radius);
            else fill(x + width * progress, y, width / bands + 1, height, color, radius);
        }
    }

    @Override
    public void shadow(float x, float y, float width, float height, int argb, float blur,
                       float offsetX, float offsetY, float radius) {
        int steps = Math.max(1, (int) Math.ceil(Math.max(1, blur) / 2f));
        for (int step = steps; step >= 1; step--) {
            float progress = step / (float) (steps + 1);
            float spread = blur * progress;
            fill(x + offsetX - spread / 2f, y + offsetY - spread / 2f,
                    width + spread, height + spread, withAlpha(argb, 1f - progress * 0.75f), radius + spread / 2f);
        }
    }

    @Override
    public void stroke(float x, float y, float width, float height, int argb, float thickness, float radius) {
        if (thickness <= 0) return;
        if (radius <= 0.5f) {
            graphics.fill((int) x, (int) y, (int) (x + width), (int) (y + thickness), argb);
            graphics.fill((int) x, (int) (y + height - thickness), (int) (x + width), (int) (y + height), argb);
            graphics.fill((int) x, (int) y, (int) (x + thickness), (int) (y + height), argb);
            graphics.fill((int) (x + width - thickness), (int) y, (int) (x + width), (int) (y + height), argb);
            return;
        }
        int rows = Math.max(1, (int) Math.ceil(height));
        for (int row = 0; row < rows; row++) {
            float inset = roundedInset(width, height, radius, row + 0.5f);
            float left = x + inset;
            float right = x + width - inset;
            if (row < thickness || row >= height - thickness) {
                graphics.fill((int) left, (int) (y + row), (int) right, (int) (y + row + 1), argb);
            } else {
                float innerWidth = Math.max(0, width - thickness * 2f);
                float innerHeight = Math.max(0, height - thickness * 2f);
                float innerRadius = Math.max(0, radius - thickness);
                float innerInset = roundedInset(innerWidth, innerHeight, innerRadius, row - thickness + 0.5f);
                graphics.fill((int) left, (int) (y + row), (int) (x + thickness + innerInset), (int) (y + row + 1), argb);
                graphics.fill((int) (x + width - thickness - innerInset), (int) (y + row), (int) right, (int) (y + row + 1), argb);
            }
        }
    }

    @Override
    public void text(String text, float x, float y, int argb, float size, String align) {
        textBox(text, x, y, 0, argb, size, align, 0, 0, 0, 0);
    }

    @Override
    public void textBox(String text, float x, float y, float width, int argb, float size, String align,
                        float letterSpacing, int shadowArgb, float shadowX, float shadowY) {
        float scale = size / 9.0f;
        MatrixTransformCompat.pushScale(graphics.pose(), scale);
        float logicalTextWidth = font.width(text) * scale + Math.max(0, text.length() - 1) * letterSpacing;
        float drawX = x;
        if (width > 0 && "center".equals(align)) drawX = x + (width - logicalTextWidth) / 2f;
        else if (width > 0 && ("right".equals(align) || "flex-end".equals(align))) drawX = x + width - logicalTextWidth;
        drawX = Math.max(x, drawX);
        if (shadowArgb != 0) drawTextLine(text, drawX + shadowX, y + shadowY, shadowArgb, letterSpacing);
        drawTextLine(text, drawX, y, argb, letterSpacing);
        MatrixTransformCompat.pop(graphics.pose());
    }

    @Override
    public void image(String resource, float x, float y, float width, float height, int tint) {
        // Mods can map image nodes to their own atlas or texture pipeline.
    }

    @Override
    public void pushClip(float x, float y, float width, float height) {
        graphics.enableScissor((int) x, (int) y, (int) (x + width), (int) (y + height));
    }

    @Override
    public void popClip() {
        graphics.disableScissor();
    }

    private void drawTextLine(String text, float x, float y, int color, float letterSpacing) {
        if (letterSpacing == 0) {
            graphics.text(font, text, (int) x, (int) y, color, false);
            return;
        }
        float cursor = x;
        for (int index = 0; index < text.length(); index++) {
            String character = text.substring(index, index + 1);
            graphics.text(font, character, (int) cursor, (int) y, color, false);
            cursor += font.width(character) + letterSpacing;
        }
    }

    private float roundedInset(float width, float height, float radius, float row) {
        float r = Math.min(radius, Math.min(width, height) / 2f);
        if (r <= 0) return 0;
        float distance = row < r ? r - row : row > height - r ? row - (height - r) : 0;
        if (distance <= 0) return 0;
        return Math.max(0, r - (float) Math.sqrt(Math.max(0, r * r - distance * distance)));
    }

    private int mix(int start, int end, float progress) {
        int alpha = mixChannel(start >>> 24, end >>> 24, progress);
        int red = mixChannel((start >>> 16) & 0xff, (end >>> 16) & 0xff, progress);
        int green = mixChannel((start >>> 8) & 0xff, (end >>> 8) & 0xff, progress);
        int blue = mixChannel(start & 0xff, end & 0xff, progress);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private int mixChannel(int start, int end, float progress) {
        return Math.round(start + (end - start) * progress);
    }

    private int withAlpha(int color, float multiplier) {
        int alpha = Math.max(0, Math.min(255, Math.round(((color >>> 24) & 0xff) * multiplier)));
        return (alpha << 24) | (color & 0x00ffffff);
    }
}
