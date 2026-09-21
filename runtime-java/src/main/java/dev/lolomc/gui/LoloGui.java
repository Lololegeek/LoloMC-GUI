package dev.lolomc.gui;

import dev.lolomc.gui.model.UiDocument;
import dev.lolomc.gui.model.UiNode;
import dev.lolomc.gui.parser.XmlUiParser;
import dev.lolomc.gui.style.CssParser;
import dev.lolomc.gui.style.StyleSheet;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class LoloGui {
    private LoloGui() {}

    public static UiDocument load(InputStream xml, String css) {
        UiNode root = new XmlUiParser().parse(xml);
        StyleSheet sheet = new CssParser().parse(css);
        return new UiDocument(root, sheet);
    }

    public static UiDocument load(String xml, String css) {
        return load(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), css);
    }

    public static GuiSession session(UiDocument document) { return new GuiSession(document); }
}

