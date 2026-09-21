package dev.lolomc.gui.render;

public interface RenderBackend {
    void fill(float x, float y, float width, float height, int argb, float radius);
    default void gradient(float x, float y, float width, float height, int startArgb, int endArgb, boolean vertical, float radius) {
        fill(x, y, width, height, startArgb, radius);
    }
    default void shadow(float x, float y, float width, float height, int argb, float blur, float offsetX, float offsetY, float radius) {}
    default void stroke(float x, float y, float width, float height, int argb, float thickness, float radius) {
        fill(x, y, width, thickness, argb, 0);
        fill(x, y + height - thickness, width, thickness, argb, 0);
        fill(x, y, thickness, height, argb, 0);
        fill(x + width - thickness, y, thickness, height, argb, 0);
    }
    void text(String text, float x, float y, int argb, float size, String align);
    default void textBox(String text, float x, float y, float width, int argb, float size, String align,
                         float letterSpacing, int shadowArgb, float shadowX, float shadowY) {
        text(text, x, y, argb, size, align);
    }
    void image(String resource, float x, float y, float width, float height, int tint);
    default void pushClip(float x, float y, float width, float height) {}
    default void popClip() {}
}
