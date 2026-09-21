package dev.lolomc.gui.model;

import dev.lolomc.gui.style.StyleSheet;

public final class UiDocument {
    private final UiNode root;
    private final StyleSheet styleSheet;

    public UiDocument(UiNode root, StyleSheet styleSheet) {
        this.root = root;
        this.styleSheet = styleSheet;
    }

    public UiNode getRoot() { return root; }
    public StyleSheet getStyleSheet() { return styleSheet; }
}

