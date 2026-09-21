package dev.lolomc.gui.style;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CssRule {
    private final String selector;
    private final Map<String, String> declarations = new LinkedHashMap<String, String>();
    private final int order;

    public CssRule(String selector, int order) {
        this.selector = selector.trim();
        this.order = order;
    }

    public String getSelector() { return selector; }
    public Map<String, String> getDeclarations() { return declarations; }
    public int getOrder() { return order; }

    public int specificity() {
        int ids = selector.length() - selector.replace("#", "").length();
        int classes = selector.length() - selector.replace(".", "").length();
        classes += selector.length() - selector.replace(":", "").length();
        int tags = 0;
        for (String part : selector.split("\\s+")) {
            if (!part.isEmpty() && Character.isLetter(part.charAt(0))) tags++;
        }
        return ids * 100 + classes * 10 + tags;
    }
}

