package dev.lolomc.gui.render;

import dev.lolomc.gui.model.Rect;
import dev.lolomc.gui.model.UiNode;
import dev.lolomc.gui.style.StyleValues;
import java.util.Locale;

/** Paints the resolved tree using only the loader adapter's small backend. */
public final class UiRenderer {
    public void render(UiNode root, RenderBackend backend) { renderNode(root, backend); }

    private void renderNode(UiNode node, RenderBackend backend) {
        Rect bounds = node.getBounds();
        if (bounds.width <= 0 || bounds.height <= 0 || "none".equals(node.getStyle().get("display"))) return;
        float opacity = StyleValues.number(node, "opacity", 1f);
        float radius = Math.max(0, Math.min(Math.min(bounds.width, bounds.height) / 2f, StyleValues.number(node, "border-radius", 0)));
        StyleValues.Shadow shadow = StyleValues.shadow(node);
        if (shadow != null) backend.shadow(bounds.x, bounds.y, bounds.width, bounds.height,
                StyleValues.withOpacity(shadow.color, opacity), shadow.blur, shadow.offsetX, shadow.offsetY, radius);

        if ("progress".equals(node.getTag())) {
            renderProgress(node, backend, opacity, radius);
        } else {
            paintBackground(node, backend, opacity, radius);
            paintBorder(node, backend, opacity, radius);
        }

        boolean clipped = "hidden".equals(node.getStyle().get("overflow"));
        if (clipped) backend.pushClip(bounds.x, bounds.y, bounds.width, bounds.height);
        String image = node.getAttributes().get("src");
        if (image != null) backend.image(image, bounds.x, bounds.y, bounds.width, bounds.height,
                StyleValues.withOpacity(StyleValues.color(node, "tint", 0xffffffff), opacity));
        paintText(node, backend, opacity);
        for (UiNode child : node.getChildren()) renderNode(child, backend);
        if (clipped) backend.popClip();
    }

    private void paintBackground(UiNode node, RenderBackend backend, float opacity, float radius) {
        Rect bounds = node.getBounds();
        StyleValues.Gradient gradient = StyleValues.gradient(node, "background");
        if (gradient != null) {
            backend.gradient(bounds.x, bounds.y, bounds.width, bounds.height,
                    StyleValues.withOpacity(gradient.start, opacity), StyleValues.withOpacity(gradient.end, opacity), gradient.vertical, radius);
            return;
        }
        int background = StyleValues.withOpacity(StyleValues.color(node, "background", 0), opacity);
        if (background != 0) backend.fill(bounds.x, bounds.y, bounds.width, bounds.height, background, radius);
    }

    private void paintBorder(UiNode node, RenderBackend backend, float opacity, float radius) {
        float border = StyleValues.number(node, "border-width", 0);
        if (border <= 0) return;
        Rect bounds = node.getBounds();
        backend.stroke(bounds.x, bounds.y, bounds.width, bounds.height,
                StyleValues.withOpacity(StyleValues.color(node, "border-color", 0xffffffff), opacity), border, radius);
    }

    private void renderProgress(UiNode node, RenderBackend backend, float opacity, float radius) {
        Rect bounds = node.getBounds();
        int track = StyleValues.withOpacity(StyleValues.color(node, "background", 0xff252a32), opacity);
        backend.fill(bounds.x, bounds.y, bounds.width, bounds.height, track, radius);
        float max = numberAttribute(node, "max", 1f);
        float value = numberAttribute(node, "value", 0f);
        float progress = max <= 0 ? 0 : Math.max(0, Math.min(1, value / max));
        int foreground = StyleValues.withOpacity(StyleValues.color(node, "progress-color", StyleValues.color(node, "color", 0xffe9a23b)), opacity);
        if (progress > 0) backend.fill(bounds.x, bounds.y, bounds.width * progress, bounds.height, foreground, radius);
        paintBorder(node, backend, opacity, radius);
    }

    private float numberAttribute(UiNode node, String name, float fallback) {
        String value = node.getAttributes().get(name);
        if (value == null || value.trim().isEmpty()) return fallback;
        try {
            String clean = value.trim();
            if (clean.endsWith("%")) return fallback * Float.parseFloat(clean.substring(0, clean.length() - 1)) / 100f;
            return Float.parseFloat(clean);
        } catch (NumberFormatException ignored) { return fallback; }
    }

    private void paintText(UiNode node, RenderBackend backend, float opacity) {
        Rect bounds = node.getBounds();
        String content = node.getText().isEmpty() ? node.getAttributes().get("text") : node.getText();
        boolean placeholder = false;
        if ("input".equals(node.getTag())) {
            content = node.attr("value", "");
            if (content.isEmpty()) {
                content = node.attr("placeholder", "");
                placeholder = !content.isEmpty();
            }
        }
        if (content == null || content.isEmpty()) return;
        String transform = node.getStyle().get("text-transform");
        if ("uppercase".equals(transform)) content = content.toUpperCase(Locale.ROOT);
        else if ("lowercase".equals(transform)) content = content.toLowerCase(Locale.ROOT);
        else if ("capitalize".equals(transform) && content.length() > 0) content = Character.toUpperCase(content.charAt(0)) + content.substring(1);

        float paddingTop = StyleValues.edge(node, "padding", 0, 0);
        float paddingRight = StyleValues.edge(node, "padding", 1, 0);
        float paddingBottom = StyleValues.edge(node, "padding", 2, 0);
        float paddingLeft = StyleValues.edge(node, "padding", 3, 0);
        float fontSize = Math.max(1, StyleValues.number(node, "font-size", 10));
        float textHeight = Math.max(fontSize, StyleValues.number(node, "line-height", fontSize));
        float contentHeight = Math.max(0, bounds.height - paddingTop - paddingBottom);
        String vertical = node.getStyle().get("vertical-align");
        if (vertical == null) vertical = "center";
        float y = bounds.y + paddingTop;
        if ("center".equals(vertical)) y += Math.max(0, contentHeight - textHeight) / 2f;
        else if ("bottom".equals(vertical) || "flex-end".equals(vertical)) y += Math.max(0, contentHeight - textHeight);
        int color = StyleValues.color(node, placeholder ? "placeholder-color" : "color", placeholder ? 0xff858b95 : 0xffffffff);
        color = StyleValues.withOpacity(color, opacity);
        StyleValues.Shadow textShadow = StyleValues.textShadow(node);
        int shadowColor = textShadow == null ? 0 : StyleValues.withOpacity(textShadow.color, opacity);
        float shadowX = textShadow == null ? 0 : textShadow.offsetX;
        float shadowY = textShadow == null ? 0 : textShadow.offsetY;
        backend.textBox(content, bounds.x + paddingLeft, y, Math.max(0, bounds.width - paddingLeft - paddingRight), color,
                fontSize, node.getStyle().containsKey("text-align") ? node.getStyle().get("text-align") : "left",
                StyleValues.number(node, "letter-spacing", 0), shadowColor, shadowX, shadowY);
    }
}
