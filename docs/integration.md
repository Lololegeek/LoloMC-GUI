# Integrating LoloMC GUI into a mod

## 1. Add the runtime

Publish `runtime-java/build/lolomc-gui-0.1.0.jar` to your local Maven repository or place it in `libs/`, then use `implementation(files("libs/lolomc-gui-0.1.0.jar"))`. No loader is a transitive runtime dependency.

Place `screen.xml` and `screen.css` in `src/main/resources/assets/<modid>/ui/`.

## 2. Create a session

```java
GuiSession ui = LoloGui.session(LoloGui.load(xmlStream, cssText))
    .on("joinServer", node -> connect())
    .on("filterServers", node -> filter(node.attr("value", "")));
```

At screen initialization and resize time:

```java
ui.resize(screenWidth, screenHeight);
```

## 3. Forward the Minecraft screen lifecycle

```java
ui.render(myBackend);
ui.mouseMoved(mouseX, mouseY);
ui.mouseClicked(mouseX, mouseY, button);
ui.mouseReleased();
ui.charTyped(character);
ui.keyPressed(keyCode); // 259 is backspace in modern mappings
```

Your `RenderBackend` maps `fill`, `text`, and `image` to `GuiGraphics` (Mojmap), `DrawContext` (Yarn), `PoseStack`, or the graphics API of the target version. Borders already have a default implementation; clipping is optional. This small class is the only mapping-dependent code.

Copy-ready recipes for modern Yarn and Mojmap are available in [`adapters/`](../adapters/README.md). They use `MinecraftScreenHost`, which forwards the complete `Screen` lifecycle without depending on a loader.

## 4. Studio workflow

Run `npm run dev -w studio`, design the screen, then click **Export**. The ZIP contains `screen.xml` and `screen.css` ready to copy into your mod resources.
