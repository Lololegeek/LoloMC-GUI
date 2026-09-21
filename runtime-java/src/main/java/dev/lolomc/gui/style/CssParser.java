package dev.lolomc.gui.style;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CssParser {
    private static final Pattern BLOCK = Pattern.compile("([^{}]+)\\{([^{}]*)\\}");

    public StyleSheet parse(String source) {
        StyleSheet sheet = new StyleSheet();
        String clean = source == null ? "" : source.replaceAll("(?s)/\\*.*?\\*/", "");
        Matcher matcher = BLOCK.matcher(clean);
        int order = 0;
        while (matcher.find()) {
            String[] selectors = matcher.group(1).trim().split(",");
            for (String selector : selectors) {
                CssRule rule = new CssRule(selector, order++);
                for (String declaration : matcher.group(2).split(";")) {
                    int colon = declaration.indexOf(':');
                    if (colon <= 0) continue;
                    String key = declaration.substring(0, colon).trim().toLowerCase();
                    String value = declaration.substring(colon + 1).trim();
                    if (!key.isEmpty() && !value.isEmpty()) rule.getDeclarations().put(key, value);
                }
                if (":root".equals(rule.getSelector())) {
                    for (String key : rule.getDeclarations().keySet()) {
                        if (key.startsWith("--")) sheet.getVariables().put(key, rule.getDeclarations().get(key));
                    }
                } else {
                    sheet.getRules().add(rule);
                }
            }
        }
        return sheet;
    }
}

