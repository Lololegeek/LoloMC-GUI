package dev.lolomc.gui.style;

import dev.lolomc.gui.model.UiNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class StyleResolver {
    private static final String[] INHERITED = {"color", "font-size", "font-family", "text-align"};

    public void resolve(UiNode root, StyleSheet sheet) { resolveNode(root, sheet); }

    private void resolveNode(UiNode node, StyleSheet sheet) {
        node.getStyle().clear();
        if (node.getParent() != null) {
            for (String key : INHERITED) {
                String value = node.getParent().getStyle().get(key);
                if (value != null) node.getStyle().put(key, value);
            }
        }
        List<CssRule> matching = new ArrayList<CssRule>();
        for (CssRule rule : sheet.getRules()) if (matches(node, rule.getSelector())) matching.add(rule);
        Collections.sort(matching, new Comparator<CssRule>() {
            public int compare(CssRule a, CssRule b) {
                int result = Integer.compare(a.specificity(), b.specificity());
                return result == 0 ? Integer.compare(a.getOrder(), b.getOrder()) : result;
            }
        });
        for (CssRule rule : matching) apply(node, rule.getDeclarations(), sheet);
        String inline = node.getAttributes().get("style");
        if (inline != null && !inline.trim().isEmpty()) {
            List<CssRule> inlineRules = new CssParser().parse("x{" + inline + "}").getRules();
            if (!inlineRules.isEmpty()) apply(node, inlineRules.get(0).getDeclarations(), sheet);
        }
        for (UiNode child : node.getChildren()) resolveNode(child, sheet);
    }

    private void apply(UiNode node, Map<String, String> declarations, StyleSheet sheet) {
        for (Map.Entry<String, String> entry : declarations.entrySet()) {
            String value = entry.getValue();
            for (Map.Entry<String, String> variable : sheet.getVariables().entrySet()) {
                value = value.replace("var(" + variable.getKey() + ")", variable.getValue());
            }
            node.getStyle().put(entry.getKey(), value);
        }
    }

    private boolean matches(UiNode node, String selector) {
        String[] parts = selector.trim().split("\\s+");
        UiNode cursor = node;
        if (!matchesPart(cursor, parts[parts.length - 1])) return false;
        for (int i = parts.length - 2; i >= 0; i--) {
            cursor = cursor.getParent();
            while (cursor != null && !matchesPart(cursor, parts[i])) cursor = cursor.getParent();
            if (cursor == null) return false;
        }
        return true;
    }

    private boolean matchesPart(UiNode node, String part) {
        String state = null;
        int pseudo = part.indexOf(':');
        if (pseudo >= 0) { state = part.substring(pseudo + 1); part = part.substring(0, pseudo); }
        String id = null;
        int hash = part.indexOf('#');
        if (hash >= 0) { id = part.substring(hash + 1).split("\\.")[0]; part = part.substring(0, hash) + part.substring(hash + 1 + id.length()); }
        String[] classParts = part.split("\\.");
        String tag = classParts.length == 0 ? "" : classParts[0];
        if (!tag.isEmpty() && !"*".equals(tag) && !tag.equals(node.getTag())) return false;
        if (id != null && !id.equals(node.getId())) return false;
        for (int i = 1; i < classParts.length; i++) if (!node.hasClass(classParts[i])) return false;
        return state == null || node.getStates().contains(state);
    }
}
