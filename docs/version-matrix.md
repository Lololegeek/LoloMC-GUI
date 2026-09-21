# Minecraft loader and version matrix

LoloMC GUI keeps its XML, CSS, layout, input, and state runtime independent from Minecraft. Each loader and Minecraft version line gets its own thin client adapter because mappings and rendering APIs are not binary-compatible across versions.

## Current example projects

| Loader | Baseline | Mapping/API | Java | Project |
|---|---:|---|---:|---|
| Fabric | 1.21.1 | Yarn / `DrawContext` | 21 | `examples/fabric-mod` |
| Forge | 1.20.1 | Mojang mappings / `GuiGraphics` | 17 | `examples/forge-mod` |
| NeoForge | 1.21.1 | Mojang mappings / `GuiGraphics` | 21 | `examples/neoforge-mod` |

## Version-line policy from 1.20 onward

The release pipeline treats these as separate compatibility lines:

- Fabric: 1.20.1, 1.20.4, 1.20.6, and each 1.21.x line that changes mappings or loader APIs.
- Forge: 1.20.1, 1.20.2, 1.20.4, 1.20.6, and each 1.21.x line that changes ForgeGradle or client APIs.
- NeoForge: 1.20.2, 1.20.4, 1.20.6, and each 1.21.x line supported by the NeoForge userdev plugin.

Each published artifact must declare its exact Minecraft version range, loader, Java level, and dependency versions. A single JAR must not claim compatibility with every version from 1.20 onward: `DrawContext`, `GuiGraphics`, key events, resource APIs, mappings, and Java requirements change between lines.

## Building the examples

Build all three checked-in baselines with:

```powershell
npm run build:loader-matrix
```

To add another version line, duplicate the loader's Gradle profile, update its Minecraft/loader/mapping versions, and run the matrix against that exact game line before uploading it to Modrinth. The shared XML/CSS files do not change.
