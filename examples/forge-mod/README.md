# LoloMC GUI — Forge example

This example opens the shared XML/CSS demo with the **O** key on Forge 1.20.1.

The Forge wrapper uses Mojang mappings and keeps the same `MinecraftScreenHost`, XML, CSS, and runtime as the Fabric example. Change `minecraft_version` and `forge_version` in `gradle.properties` for another compatible Forge line, then verify that line before publishing its artifact.

```powershell
gradlew.bat build
gradlew.bat runClient
```
