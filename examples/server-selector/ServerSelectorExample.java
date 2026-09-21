package example;

import dev.lolomc.gui.GuiSession;
import dev.lolomc.gui.LoloGui;
import dev.lolomc.gui.model.UiDocument;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class ServerSelectorExample {
    public static GuiSession create() {
        InputStream xml = ServerSelectorExample.class.getResourceAsStream("/assets/example/ui/screen.xml");
        InputStream cssStream = ServerSelectorExample.class.getResourceAsStream("/assets/example/ui/screen.css");
        String css = new Scanner(cssStream, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
        UiDocument document = LoloGui.load(xml, css);
        return LoloGui.session(document)
                .on("joinServer", source -> System.out.println("Join requested"))
                .on("filterServers", source -> System.out.println(source.attr("value", "")));
    }
}

