package dev.lolomc.gui;

import dev.lolomc.gui.model.UiDocument;
import dev.lolomc.gui.model.UiNode;
import dev.lolomc.gui.render.RenderBackend;
import java.util.ArrayList;
import java.util.List;

public final class RuntimeTest {
    public static void main(String[] args) {
        parsesStylesLayoutsAndClicks();
        handlesEmptyInlineStyleAndOutOfFlowChildren();
        rejectsDoctype();
        System.out.println("LoloMC GUI runtime tests passed");
    }

    private static void handlesEmptyInlineStyleAndOutOfFlowChildren() {
        UiDocument document = LoloGui.load("<screen><panel id='flow' style=''/><panel style='display:none'/><panel style='position:absolute'/></screen>", "screen{gap:10} panel{height:20}");
        GuiSession session = LoloGui.session(document);
        session.resize(100, 100);
        assert document.getRoot().findById("flow").getBounds().y == 0;
    }

    private static void parsesStylesLayoutsAndClicks() {
        String xml = "<screen id='root'><panel class='card'><label id='title'>Serveurs</label><button id='play' on-click='join'>Jouer</button><input id='search' on-change='search'/></panel></screen>";
        String css = ":root{--accent:#e9a23b} screen{padding:10;gap:4;background:#101116} .card{height:100;gap:5} #title{height:20;color:var(--accent)} button{height:30;background:#333333} button:hover{background:#555555}";
        UiDocument document = LoloGui.load(xml, css);
        final int[] clicks = {0};
        GuiSession session = LoloGui.session(document).on("join", new GuiAction() {
            public void run(UiNode source) { clicks[0]++; }
        });
        session.resize(320, 180);
        UiNode play = document.getRoot().findById("play");
        assert play != null;
        assert play.getBounds().height == 30;
        session.mouseMoved(play.getBounds().x + 1, play.getBounds().y + 1);
        assert "#555555".equals(play.getStyle().get("background"));
        assert session.mouseClicked(play.getBounds().x + 1, play.getBounds().y + 1, 0);
        assert clicks[0] == 1;
        UiNode search = document.getRoot().findById("search");
        session.mouseClicked(search.getBounds().x + 1, search.getBounds().y + 1, 0);
        assert session.charTyped('a');
        assert "a".equals(search.getAttributes().get("value"));
        assert session.keyPressed(259);
        assert "".equals(search.getAttributes().get("value"));
        RecordingBackend backend = new RecordingBackend();
        session.render(backend);
        assert backend.calls.size() >= 4;
    }

    private static void rejectsDoctype() {
        boolean rejected = false;
        try { LoloGui.load("<!DOCTYPE x [<!ENTITY e SYSTEM 'file:///x'>]><screen>&e;</screen>", ""); }
        catch (IllegalArgumentException expected) { rejected = true; }
        assert rejected;
    }

    private static final class RecordingBackend implements RenderBackend {
        final List<String> calls = new ArrayList<String>();
        public void fill(float x, float y, float w, float h, int color, float radius) { calls.add("fill"); }
        public void stroke(float x, float y, float w, float h, int color, float thickness, float radius) { calls.add("stroke"); }
        public void text(String text, float x, float y, int color, float size, String align) { calls.add("text:" + text); }
        public void image(String resource, float x, float y, float w, float h, int tint) { calls.add("image"); }
        public void pushClip(float x, float y, float w, float h) { calls.add("clip"); }
        public void popClip() { calls.add("pop"); }
    }
}
