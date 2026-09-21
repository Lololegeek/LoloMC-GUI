# LoloMC GUI — Fabric example mod

This project opens a real Minecraft GUI with the **O** key.

The mod loads:

- `src/main/resources/assets/lolomc_gui_example/ui/screen.xml`
- `src/main/resources/assets/lolomc_gui_example/ui/screen.css`

It creates a `GuiSession`, connects the `play`, `nameChanged`, and `close` actions, and delegates the `Screen` lifecycle to `MinecraftScreenHost`.

## Build

From this directory:

```powershell
gradlew.bat build
```

The parent `runtime-java` project is consumed automatically through `includeBuild`. The final JAR is written to `build/libs/`.

## Run the Minecraft development client

```powershell
gradlew.bat runClient
```

This module targets Fabric/Minecraft 1.21.1 with Yarn mappings. Rendering APIs change between versions, but the XML/CSS files and `GuiSession` remain the same.
