package dev.lolomc.gui.layout;

import dev.lolomc.gui.model.Rect;
import dev.lolomc.gui.model.UiNode;
import dev.lolomc.gui.style.StyleValues;
import java.util.ArrayList;
import java.util.List;

/** Small, deterministic flex layout engine designed for Minecraft screens. */
public final class LayoutEngine {
    public void layout(UiNode root, float width, float height) {
        root.getBounds().x = 0;
        root.getBounds().y = 0;
        root.getBounds().width = Math.max(0, width);
        root.getBounds().height = Math.max(0, height);
        layoutChildren(root);
    }

    private void layoutChildren(UiNode parent) {
        Rect area = parent.getBounds();
        float paddingTop = StyleValues.edge(parent, "padding", 0, 0);
        float paddingRight = StyleValues.edge(parent, "padding", 1, 0);
        float paddingBottom = StyleValues.edge(parent, "padding", 2, 0);
        float paddingLeft = StyleValues.edge(parent, "padding", 3, 0);
        float innerWidth = Math.max(0, area.width - paddingLeft - paddingRight);
        float innerHeight = Math.max(0, area.height - paddingTop - paddingBottom);
        boolean row = "row".equals(parent.getStyle().get("flex-direction"));
        float gap = StyleValues.number(parent, "gap", 0);
        List<UiNode> flow = new ArrayList<UiNode>();
        for (UiNode child : parent.getChildren()) {
            if (!"none".equals(child.getStyle().get("display")) && !"absolute".equals(child.getStyle().get("position"))) flow.add(child);
        }

        float fixed = gap * Math.max(0, flow.size() - 1);
        float flexTotal = 0;
        float[] widths = new float[flow.size()];
        float[] heights = new float[flow.size()];
        float[] marginsMainStart = new float[flow.size()];
        float[] marginsMainEnd = new float[flow.size()];
        float[] marginsCrossStart = new float[flow.size()];
        float[] marginsCrossEnd = new float[flow.size()];

        for (int i = 0; i < flow.size(); i++) {
            UiNode child = flow.get(i);
            float marginTop = StyleValues.edge(child, "margin", 0, 0);
            float marginRight = StyleValues.edge(child, "margin", 1, 0);
            float marginBottom = StyleValues.edge(child, "margin", 2, 0);
            float marginLeft = StyleValues.edge(child, "margin", 3, 0);
            marginsMainStart[i] = row ? marginLeft : marginTop;
            marginsMainEnd[i] = row ? marginRight : marginBottom;
            marginsCrossStart[i] = row ? marginTop : marginLeft;
            marginsCrossEnd[i] = row ? marginBottom : marginRight;
            widths[i] = StyleValues.size(child, "width", innerWidth, row ? defaultMain(child, true) : innerWidth);
            heights[i] = StyleValues.size(child, "height", innerHeight, row ? innerHeight : defaultMain(child, false));
            float flex = StyleValues.number(child, "flex", 0);
            if (flex > 0) flexTotal += flex;
            else fixed += (row ? widths[i] : heights[i]) + marginsMainStart[i] + marginsMainEnd[i];
        }

        float mainSize = row ? innerWidth : innerHeight;
        float remaining = Math.max(0, mainSize - fixed);
        if (flexTotal > 0) {
            for (int i = 0; i < flow.size(); i++) {
                float flex = StyleValues.number(flow.get(i), "flex", 0);
                if (flex > 0) {
                    float value = remaining * flex / flexTotal;
                    if (row) widths[i] = Math.max(0, value - marginsMainStart[i] - marginsMainEnd[i]);
                    else heights[i] = Math.max(0, value - marginsMainStart[i] - marginsMainEnd[i]);
                }
            }
        }

        float occupied = gap * Math.max(0, flow.size() - 1);
        for (int i = 0; i < flow.size(); i++) occupied += (row ? widths[i] : heights[i]) + marginsMainStart[i] + marginsMainEnd[i];
        float free = Math.max(0, mainSize - occupied);
        String justify = parent.getStyle().get("justify-content");
        if (justify == null) justify = "flex-start";
        float cursor = (row ? area.x + paddingLeft : area.y + paddingTop) + justifyOffset(justify, free, flow.size());
        float between = justifyBetween(justify, free, flow.size());

        for (UiNode child : parent.getChildren()) {
            Rect bounds = child.getBounds();
            if ("none".equals(child.getStyle().get("display"))) {
                bounds.width = 0;
                bounds.height = 0;
                continue;
            }
            if ("absolute".equals(child.getStyle().get("position"))) {
                float width = StyleValues.size(child, "width", innerWidth, defaultMain(child, true));
                float height = StyleValues.size(child, "height", innerHeight, defaultMain(child, false));
                float left = StyleValues.number(child, "left", paddingLeft);
                float top = StyleValues.number(child, "top", paddingTop);
                if (child.getStyle().containsKey("right")) left = area.width - paddingRight - width - StyleValues.number(child, "right", 0);
                if (child.getStyle().containsKey("bottom")) top = area.height - paddingBottom - height - StyleValues.number(child, "bottom", 0);
                bounds.x = area.x + left;
                bounds.y = area.y + top;
                bounds.width = Math.max(0, width);
                bounds.height = Math.max(0, height);
                layoutChildren(child);
            }
        }

        int index = 0;
        for (UiNode child : flow) {
            float width = bounded(child, "width", widths[index], innerWidth);
            float height = bounded(child, "height", heights[index], innerHeight);
            float mainStart = marginsMainStart[index];
            float mainEnd = marginsMainEnd[index];
            String align = child.getStyle().get("align-self");
            if (align == null) align = parent.getStyle().get("align-items");
            if (align == null) align = "stretch";
            float crossAvailable = row ? innerHeight : innerWidth;
            float crossSize = row ? height : width;
            boolean explicitCross = row ? child.getStyle().containsKey("height") : child.getStyle().containsKey("width");
            if ("stretch".equals(align) && !explicitCross) crossSize = Math.max(0, crossAvailable - marginsCrossStart[index] - marginsCrossEnd[index]);
            float crossOffset = crossOffset(align, crossAvailable, crossSize, marginsCrossStart[index], marginsCrossEnd[index]);

            Rect bounds = child.getBounds();
            if (row) {
                bounds.x = cursor + mainStart;
                bounds.y = area.y + paddingTop + crossOffset;
                bounds.width = width;
                bounds.height = crossSize;
                cursor += mainStart + width + mainEnd + gap + between;
            } else {
                bounds.x = area.x + paddingLeft + crossOffset;
                bounds.y = cursor + mainStart;
                bounds.width = crossSize;
                bounds.height = height;
                cursor += mainStart + height + mainEnd + gap + between;
            }
            layoutChildren(child);
            index++;
        }
    }

    private float bounded(UiNode node, String key, float value, float parent) {
        float min = StyleValues.size(node, "min-" + key, parent, 0);
        float max = StyleValues.size(node, "max-" + key, parent, Float.MAX_VALUE);
        return Math.max(min, Math.min(max, Math.max(0, value)));
    }

    private float justifyOffset(String justify, float free, int count) {
        if ("center".equals(justify)) return free / 2f;
        if ("flex-end".equals(justify) || "end".equals(justify)) return free;
        if ("space-around".equals(justify)) return count == 0 ? 0 : free / (count * 2f);
        if ("space-evenly".equals(justify)) return count == 0 ? 0 : free / (count + 1f);
        return 0;
    }

    private float justifyBetween(String justify, float free, int count) {
        if (count < 2) return 0;
        if ("space-between".equals(justify)) return free / (count - 1f);
        if ("space-around".equals(justify)) return free / count;
        if ("space-evenly".equals(justify)) return free / (count + 1f);
        return 0;
    }

    private float crossOffset(String align, float available, float size, float start, float end) {
        float free = Math.max(0, available - size - start - end);
        if ("center".equals(align)) return start + free / 2f;
        if ("flex-end".equals(align) || "end".equals(align)) return start + free;
        return start;
    }

    private float defaultMain(UiNode node, boolean row) {
        if (row) return "button".equals(node.getTag()) ? 120 : 80;
        if ("label".equals(node.getTag()) || "text".equals(node.getTag())) return 20;
        if ("button".equals(node.getTag()) || "input".equals(node.getTag())) return 36;
        if ("progress".equals(node.getTag()) || "divider".equals(node.getTag())) return 8;
        return 48;
    }
}
