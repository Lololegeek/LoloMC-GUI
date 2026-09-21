package dev.lolomc.gui.style;

import dev.lolomc.gui.model.UiNode;

public final class StyleValues {
    private StyleValues() {}

    public static float number(UiNode node, String key, float fallback) {
        String value = node.getStyle().get(key);
        if (value == null || "auto".equals(value)) return fallback;
        try { return Float.parseFloat(value.replace("px", "").trim()); }
        catch (NumberFormatException ignored) { return fallback; }
    }

    public static float size(UiNode node, String key, float parent, float fallback) {
        String value = node.getStyle().get(key);
        if (value == null || "auto".equals(value)) return fallback;
        if (value.endsWith("%")) {
            try { return parent * Float.parseFloat(value.substring(0, value.length() - 1)) / 100f; }
            catch (NumberFormatException ignored) { return fallback; }
        }
        return number(node, key, fallback);
    }

    public static int color(UiNode node, String key, int fallback) {
        String value = node.getStyle().get(key);
        if (value == null || "transparent".equals(value)) return fallback;
        if (value.startsWith("#")) {
            String hex = value.substring(1);
            if (hex.length() == 3) hex = "" + hex.charAt(0) + hex.charAt(0) + hex.charAt(1) + hex.charAt(1) + hex.charAt(2) + hex.charAt(2);
            if (hex.length() == 6) hex = "ff" + hex;
            try { return (int) Long.parseLong(hex, 16); } catch (NumberFormatException ignored) { return fallback; }
        }
        return fallback;
    }
}

