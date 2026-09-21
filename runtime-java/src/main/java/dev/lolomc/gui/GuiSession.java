package dev.lolomc.gui;

import dev.lolomc.gui.layout.LayoutEngine;
import dev.lolomc.gui.model.UiDocument;
import dev.lolomc.gui.model.UiNode;
import dev.lolomc.gui.render.RenderBackend;
import dev.lolomc.gui.render.UiRenderer;
import dev.lolomc.gui.style.StyleResolver;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GuiSession {
    private final UiDocument document;
    private final Map<String, GuiAction> actions = new LinkedHashMap<String, GuiAction>();
    private final StyleResolver styles = new StyleResolver();
    private final LayoutEngine layout = new LayoutEngine();
    private final UiRenderer renderer = new UiRenderer();
    private float width;
    private float height;
    private UiNode focused;

    GuiSession(UiDocument document) { this.document = document; }
    public UiDocument getDocument() { return document; }
    public GuiSession on(String name, GuiAction action) { actions.put(name, action); return this; }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
        refresh();
    }

    public void refresh() {
        styles.resolve(document.getRoot(), document.getStyleSheet());
        layout.layout(document.getRoot(), width, height);
    }

    public void render(RenderBackend backend) { renderer.render(document.getRoot(), backend); }

    public void mouseMoved(float x, float y) {
        updateHover(document.getRoot(), x, y);
        refresh();
    }

    public boolean mouseClicked(float x, float y, int button) {
        UiNode hit = hit(document.getRoot(), x, y);
        if (hit == null) return false;
        if (focused != null) focused.state("focus", false);
        focused = "input".equals(hit.getTag()) ? hit : null;
        if (focused != null) focused.state("focus", true);
        hit.state("active", true);
        String actionName = hit.getAttributes().get("on-click");
        GuiAction action = actions.get(actionName);
        if (action != null) action.run(hit);
        refresh();
        return action != null || focused != null;
    }

    public void mouseReleased() { clearState(document.getRoot(), "active"); refresh(); }

    public boolean charTyped(char character) {
        if (focused == null || Character.isISOControl(character)) return false;
        focused.getAttributes().put("value", focused.attr("value", "") + character);
        fireChange();
        refresh();
        return true;
    }

    public boolean keyPressed(int keyCode) {
        if (focused == null || keyCode != 259) return false;
        String value = focused.attr("value", "");
        if (!value.isEmpty()) focused.getAttributes().put("value", value.substring(0, value.length() - 1));
        fireChange();
        refresh();
        return true;
    }

    private void fireChange() {
        GuiAction action = actions.get(focused.getAttributes().get("on-change"));
        if (action != null) action.run(focused);
    }

    private void updateHover(UiNode node, float x, float y) {
        node.state("hover", node.getBounds().contains(x, y));
        for (UiNode child : node.getChildren()) updateHover(child, x, y);
    }

    private void clearState(UiNode node, String state) {
        node.state(state, false);
        for (UiNode child : node.getChildren()) clearState(child, state);
    }

    private UiNode hit(UiNode node, float x, float y) {
        for (int i = node.getChildren().size() - 1; i >= 0; i--) {
            UiNode found = hit(node.getChildren().get(i), x, y);
            if (found != null) return found;
        }
        return node.getBounds().contains(x, y) ? node : null;
    }
}
