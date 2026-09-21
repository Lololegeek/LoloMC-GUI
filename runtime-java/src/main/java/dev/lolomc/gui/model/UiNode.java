package dev.lolomc.gui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class UiNode {
    private final String tag;
    private final Map<String, String> attributes = new LinkedHashMap<String, String>();
    private final Map<String, String> style = new LinkedHashMap<String, String>();
    private final List<UiNode> children = new ArrayList<UiNode>();
    private final Set<String> states = new LinkedHashSet<String>();
    private final Rect bounds = new Rect();
    private UiNode parent;
    private String text = "";

    public UiNode(String tag) { this.tag = tag.toLowerCase(); }
    public String getTag() { return tag; }
    public String getId() { return attributes.get("id"); }
    public String getText() { return text; }
    public void setText(String text) { this.text = text == null ? "" : text; }
    public UiNode getParent() { return parent; }
    public Rect getBounds() { return bounds; }
    public List<UiNode> getChildren() { return Collections.unmodifiableList(children); }
    public Map<String, String> getAttributes() { return attributes; }
    public Map<String, String> getStyle() { return style; }
    public Set<String> getStates() { return states; }

    public void add(UiNode child) {
        child.parent = this;
        children.add(child);
    }

    public boolean hasClass(String name) {
        String classes = attributes.get("class");
        return classes != null && Arrays.asList(classes.trim().split("\\s+")).contains(name);
    }

    public String attr(String name, String fallback) {
        String value = attributes.get(name);
        return value == null ? fallback : value;
    }

    public void state(String name, boolean active) {
        if (active) states.add(name); else states.remove(name);
    }

    public UiNode findById(String id) {
        if (id != null && id.equals(getId())) return this;
        for (UiNode child : children) {
            UiNode found = child.findById(id);
            if (found != null) return found;
        }
        return null;
    }
}

