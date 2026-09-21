package dev.lolomc.gui.style;

import dev.lolomc.gui.model.UiNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class StyleValues {
    private StyleValues() {}

    public static float number(UiNode node, String key, float fallback) {
        String value = node.getStyle().get(key);
        if (value == null || "auto".equals(value)) return fallback;
        try { return Float.parseFloat(value.replace("px", "").trim()); }
        catch (NumberFormatException ignored) { return fallback; }
    }

    /** Reads a CSS-like edge shorthand: 1, 2, 3 or 4 values. */
    public static float edge(UiNode node, String key, int side, float fallback) {
        String value = node.getStyle().get(key);
        if (value == null || value.trim().isEmpty()) return fallback;
        String[] values = value.trim().split("\\s+");
        int index;
        if (values.length == 1) index = 0;
        else if (values.length == 2) index = side == 0 || side == 2 ? 0 : 1;
        else if (values.length == 3) index = side == 0 ? 0 : side == 1 || side == 3 ? 1 : 2;
        else index = Math.min(side, values.length - 1);
        try { return Float.parseFloat(values[index].replace("px", "").trim()); }
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
        return parseColor(node.getStyle().get(key), fallback);
    }

    public static int parseColor(String raw, int fallback) {
        if (raw == null) return fallback;
        String value = raw.trim().toLowerCase(Locale.ROOT);
        if ("transparent".equals(value)) return 0;
        if (value.startsWith("#")) {
            String hex = value.substring(1);
            if (hex.length() == 3) hex = "" + hex.charAt(0) + hex.charAt(0) + hex.charAt(1) + hex.charAt(1) + hex.charAt(2) + hex.charAt(2);
            try {
                if (hex.length() == 6) return (int) Long.parseLong("ff" + hex, 16);
                // CSS uses #RRGGBBAA; RenderBackend uses Minecraft's ARGB.
                if (hex.length() == 8) {
                    long rgba = Long.parseLong(hex, 16);
                    return (int) (((rgba & 0xffL) << 24) | ((rgba >>> 8) & 0x00ffffffL));
                }
            } catch (NumberFormatException ignored) { return fallback; }
        }
        if (value.startsWith("rgb(" ) || value.startsWith("rgba(")) {
            int open = value.indexOf('(');
            int close = value.lastIndexOf(')');
            if (close > open) {
                String[] channels = value.substring(open + 1, close).split(",");
                try {
                    int red = channel(channels[0]);
                    int green = channel(channels[1]);
                    int blue = channel(channels[2]);
                    int alpha = channels.length > 3 ? alpha(channels[3]) : 255;
                    return (alpha << 24) | (red << 16) | (green << 8) | blue;
                } catch (RuntimeException ignored) { return fallback; }
            }
        }
        if ("black".equals(value)) return 0xff000000;
        if ("white".equals(value)) return 0xffffffff;
        if ("gray".equals(value) || "grey".equals(value)) return 0xff808080;
        if ("red".equals(value)) return 0xffff0000;
        if ("green".equals(value)) return 0xff00ff00;
        if ("blue".equals(value)) return 0xff0000ff;
        if ("yellow".equals(value)) return 0xffffff00;
        return fallback;
    }

    private static int channel(String value) {
        String clean = value.trim();
        if (clean.endsWith("%")) return clamp(Math.round(Float.parseFloat(clean.substring(0, clean.length() - 1)) * 2.55f), 0, 255);
        return clamp(Math.round(Float.parseFloat(clean)), 0, 255);
    }

    private static int alpha(String value) {
        String clean = value.trim();
        if (clean.endsWith("%")) return clamp(Math.round(Float.parseFloat(clean.substring(0, clean.length() - 1)) * 2.55f), 0, 255);
        float parsed = Float.parseFloat(clean);
        return clamp(Math.round(parsed <= 1f ? parsed * 255f : parsed), 0, 255);
    }

    private static int clamp(int value, int min, int max) { return Math.max(min, Math.min(max, value)); }

    public static int withOpacity(int color, float opacity) {
        int alpha = clamp(Math.round(((color >>> 24) & 0xff) * Math.max(0f, Math.min(1f, opacity))), 0, 255);
        return (alpha << 24) | (color & 0x00ffffff);
    }

    public static Gradient gradient(UiNode node, String key) {
        String value = node.getStyle().get(key);
        if (value == null) return null;
        String clean = value.trim();
        if (!clean.toLowerCase(Locale.ROOT).startsWith("linear-gradient(")) return null;
        int open = clean.indexOf('(');
        int close = clean.lastIndexOf(')');
        if (close <= open) return null;
        List<String> parts = splitTopLevel(clean.substring(open + 1, close));
        if (parts.size() < 2) return null;
        boolean vertical = true;
        String direction = parts.get(0).trim().toLowerCase(Locale.ROOT);
        if (direction.endsWith("deg") || direction.startsWith("to ")) {
            vertical = direction.endsWith("deg") ? !(direction.startsWith("90") || direction.startsWith("270")) : !direction.contains("right") && !direction.contains("left");
            parts.remove(0);
        }
        if (parts.size() < 2) return null;
        return new Gradient(parseColor(parts.get(0), 0), parseColor(parts.get(parts.size() - 1), 0), vertical);
    }

    private static List<String> splitTopLevel(String value) {
        List<String> result = new ArrayList<String>();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (character == '(') depth++;
            else if (character == ')') depth--;
            else if (character == ',' && depth == 0) {
                result.add(value.substring(start, i).trim());
                start = i + 1;
            }
        }
        result.add(value.substring(start).trim());
        return result;
    }

    public static Shadow shadow(UiNode node) {
        return parseShadow(node.getStyle().get("box-shadow"), true);
    }

    public static Shadow textShadow(UiNode node) {
        return parseShadow(node.getStyle().get("text-shadow"), false);
    }

    private static Shadow parseShadow(String value, boolean withBlur) {
        if (value == null || "none".equalsIgnoreCase(value.trim())) return null;
        List<String> parts = splitWhitespaceOutsideParentheses(value.trim());
        if (parts.size() < (withBlur ? 3 : 2)) return null;
        try {
            float offsetX = number(parts.get(0));
            float offsetY = number(parts.get(1));
            float blur = withBlur ? number(parts.get(2)) : 0;
            int color = parseColor(parts.size() > (withBlur ? 3 : 2) ? parts.get(parts.size() - 1) : "#00000080", 0x80000000);
            return new Shadow(offsetX, offsetY, Math.max(0, blur), color);
        } catch (NumberFormatException ignored) { return null; }
    }

    private static float number(String value) { return Float.parseFloat(value.replace("px", "")); }

    private static List<String> splitWhitespaceOutsideParentheses(String value) {
        List<String> result = new ArrayList<String>();
        int depth = 0;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (character == '(') depth++;
            if (character == ')') depth--;
            if (Character.isWhitespace(character) && depth == 0) {
                if (current.length() > 0) { result.add(current.toString()); current.setLength(0); }
            } else current.append(character);
        }
        if (current.length() > 0) result.add(current.toString());
        return result;
    }

    public static final class Gradient {
        public final int start;
        public final int end;
        public final boolean vertical;

        public Gradient(int start, int end, boolean vertical) {
            this.start = start;
            this.end = end;
            this.vertical = vertical;
        }
    }

    public static final class Shadow {
        public final float offsetX;
        public final float offsetY;
        public final float blur;
        public final int color;

        public Shadow(float offsetX, float offsetY, float blur, int color) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.blur = blur;
            this.color = color;
        }
    }
}
