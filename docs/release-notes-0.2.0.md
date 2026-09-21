# LoloMC GUI 0.2.0

This release adds verified Minecraft loader example projects for the version families supported from Minecraft 1.20 onward.

## Included artifacts

- Fabric 1.21.1, Java 21, Yarn mappings.
- Forge 1.20.1, Java 17, official Mojang mappings.
- NeoForge 1.21.1, Java 21, official Mojang mappings.

Each example uses the same LoloMC GUI runtime, XML screen document, CSS stylesheet, responsive layout, keyboard shortcut, and interactive screen host. The loader adapters stay isolated so version-specific mappings do not leak into the shared runtime.

All three Gradle builds are verified by the GitHub Actions loader matrix. Later Minecraft patch lines should use the matching adapter family and update only the loader example coordinates.
