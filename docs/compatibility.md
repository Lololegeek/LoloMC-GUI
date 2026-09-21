# Compatibility

## Core 0.1 support

| Environment | Status | Why |
|---|---|---|
| Java 8 to 21+ | Compatible | bytecode is compiled with `--release 8`, standard API only |
| Fabric / Quilt | Compatible through an adapter | no loader, mixin, or mapping dependency |
| Forge / NeoForge | Compatible through an adapter | no loader, event, or mapping dependency |
| Minecraft 1.12.2 to 1.21.x | Architecture-compatible | version-specific rendering calls are isolated in `RenderBackend` |
| Vanilla client without a loader | Possible | requires an injection point supplied by the integrator |

Compatibility does not mean that one mod JAR targeting obfuscated Minecraft classes will run on every version. LoloMC GUI makes the **document, styles, layout, and interactions** universal; the mod only supplies six rendering primitives and forwards its screen events.

The included example and its build validation target Fabric/Minecraft 1.21.1. It is an executable reference for other families; their graphics APIs must still be adapted to their mappings.

## Recommended adapter families

- 1.12.2–1.16.5: backend based on `MatrixStack` and historical primitives.
- 1.17–1.19.4: `PoseStack` or `MatrixStack` backend depending on mappings.
- 1.20–1.21.x: `GuiGraphics` (Mojmap) or `DrawContext` (Yarn) backend.

The runtime stays identical between these modules. Only the adapter file changes.
