package dev.lolomc.gui.layout;

import dev.lolomc.gui.model.Rect;
import dev.lolomc.gui.model.UiNode;
import dev.lolomc.gui.style.StyleValues;
import java.util.List;

public final class LayoutEngine {
    public void layout(UiNode root, float width, float height) {
        root.getBounds().x = 0;
        root.getBounds().y = 0;
        root.getBounds().width = width;
        root.getBounds().height = height;
        layoutChildren(root);
    }

    private void layoutChildren(UiNode parent) {
        Rect area = parent.getBounds();
        float padding = StyleValues.number(parent, "padding", 0);
        float gap = StyleValues.number(parent, "gap", 0);
        boolean row = "row".equals(parent.getStyle().get("flex-direction"));
        List<UiNode> children = parent.getChildren();
        float innerWidth = Math.max(0, area.width - padding * 2);
        float innerHeight = Math.max(0, area.height - padding * 2);
        int flowCount = 0;
        for (UiNode child : children) {
            if (!"none".equals(child.getStyle().get("display")) && !"absolute".equals(child.getStyle().get("position"))) flowCount++;
        }
        float fixed = gap * Math.max(0, flowCount - 1);
        float flexTotal = 0;
        for (UiNode child : children) {
            if ("none".equals(child.getStyle().get("display")) || "absolute".equals(child.getStyle().get("position"))) continue;
            float flex = StyleValues.number(child, "flex", 0);
            if (flex > 0) flexTotal += flex;
            else fixed += StyleValues.size(child, row ? "width" : "height", row ? innerWidth : innerHeight, defaultMain(child, row));
        }
        float cursor = row ? area.x + padding : area.y + padding;
        float remaining = Math.max(0, (row ? innerWidth : innerHeight) - fixed);
        for (UiNode child : children) {
            Rect bounds = child.getBounds();
            if ("none".equals(child.getStyle().get("display"))) { bounds.width = bounds.height = 0; continue; }
            float width = StyleValues.size(child, "width", innerWidth, row ? defaultMain(child, true) : innerWidth);
            float height = StyleValues.size(child, "height", innerHeight, row ? innerHeight : defaultMain(child, false));
            float flex = StyleValues.number(child, "flex", 0);
            if (flex > 0 && flexTotal > 0) { if (row) width = remaining * flex / flexTotal; else height = remaining * flex / flexTotal; }
            if ("absolute".equals(child.getStyle().get("position"))) {
                bounds.x = area.x + StyleValues.number(child, "left", padding);
                bounds.y = area.y + StyleValues.number(child, "top", padding);
            } else if (row) {
                bounds.x = cursor; bounds.y = area.y + padding; cursor += width + gap;
            } else {
                bounds.x = area.x + padding; bounds.y = cursor; cursor += height + gap;
            }
            bounds.width = Math.max(0, width);
            bounds.height = Math.max(0, height);
            layoutChildren(child);
        }
    }

    private float defaultMain(UiNode node, boolean row) {
        if (row) return "button".equals(node.getTag()) ? 120 : 80;
        if ("label".equals(node.getTag()) || "text".equals(node.getTag())) return 20;
        if ("button".equals(node.getTag()) || "input".equals(node.getTag())) return 36;
        return 48;
    }
}
