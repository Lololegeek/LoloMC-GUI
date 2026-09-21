package dev.lolomc.gui.render;

import dev.lolomc.gui.model.Rect;
import dev.lolomc.gui.model.UiNode;
import dev.lolomc.gui.style.StyleValues;

public final class UiRenderer {
    public void render(UiNode root, RenderBackend backend) { renderNode(root, backend); }

    private void renderNode(UiNode node, RenderBackend backend) {
        Rect b = node.getBounds();
        if (b.width <= 0 || b.height <= 0 || "none".equals(node.getStyle().get("display"))) return;
        float radius = StyleValues.number(node, "border-radius", 0);
        int background = StyleValues.color(node, "background", 0);
        if (background != 0) backend.fill(b.x, b.y, b.width, b.height, background, radius);
        float border = StyleValues.number(node, "border-width", 0);
        if (border > 0) backend.stroke(b.x, b.y, b.width, b.height, StyleValues.color(node, "border-color", 0xffffffff), border, radius);
        boolean clipped = "hidden".equals(node.getStyle().get("overflow"));
        if (clipped) backend.pushClip(b.x, b.y, b.width, b.height);
        String image = node.getAttributes().get("src");
        if (image != null) backend.image(image, b.x, b.y, b.width, b.height, StyleValues.color(node, "tint", 0xffffffff));
        String content = node.getText().isEmpty() ? node.getAttributes().get("text") : node.getText();
        if ("input".equals(node.getTag())) content = node.attr("value", node.attr("placeholder", ""));
        if (content != null && !content.isEmpty()) {
            float padding = StyleValues.number(node, "padding", 0);
            backend.text(content, b.x + padding, b.y + (b.height - StyleValues.number(node, "font-size", 10)) / 2f,
                    StyleValues.color(node, "color", 0xffffffff), StyleValues.number(node, "font-size", 10), node.getStyle().containsKey("text-align") ? node.getStyle().get("text-align") : "left");
        }
        for (UiNode child : node.getChildren()) renderNode(child, backend);
        if (clipped) backend.popClip();
    }
}
