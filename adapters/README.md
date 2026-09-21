# Minecraft adapters

These recipes are the concrete bridge between `MinecraftScreenHost` and the version-specific Minecraft `Screen` classes.

- `fabric-yarn-1.20-1.21`: Fabric or Quilt with Yarn mappings and `DrawContext`.
- `forge-neoforge-mojmap-1.20-1.21`: Forge or NeoForge with Mojmap and `GuiGraphics`.

Copy both templates from your target family into the client source set, replace the package, then open the screen normally:

```java
MinecraftClient.getInstance().setScreen(new LoloScreen(session)); // Yarn
Minecraft.getInstance().setScreen(new LoloScreen(session));       // Mojmap
```

Texture methods intentionally have a clear integration point: their signatures change most often, and mods frequently use their own atlas/cache. Text, rectangles, borders, clipping, and all input forwarding are already wired.

For 1.12–1.19, keep `MinecraftScreenHost` and only replace the graphics imports/classes with `GuiScreen`, `MatrixStack`, or `PoseStack`. The runtime and XML/CSS files stay unchanged.
