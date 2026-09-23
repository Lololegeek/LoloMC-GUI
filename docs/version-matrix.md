# Minecraft loader and version matrix

LoloMC GUI's XML, CSS, layout, input, and state runtime is loader-neutral. The Minecraft screen and rendering adapter is compiled for an exact game version and loader family; a single Minecraft-dependent JAR is not compatible with every release.

## Minecraft 1.20.1–1.21.11

The GitHub Actions matrix builds a separate client example for every upstream release line where that loader publishes a compatible development artifact:

| Loader | Minecraft release lines | Java bytecode | Mapping family |
|---|---|---:|---|
| Fabric | 1.20.1–1.20.6, 1.21–1.21.11 | 17 through 1.20.4; 21 from 1.20.5 | Yarn |
| Forge | 1.20.1–1.20.4, 1.20.6, 1.21, 1.21.1, 1.21.3–1.21.11 | 17 through 1.20.4; 21 from 1.20.6 | Mojang |
| NeoForge | 1.20.1–1.20.6, 1.21–1.21.11 | 17 through 1.20.4; 21 from 1.20.5 | Mojang |

Forge does not publish Minecraft development coordinates for 1.20.5 or 1.21.2, so those two Forge artifacts cannot be built. The NeoForge 1.20.1 example uses the legacy `net.neoforged:forge` coordinate; NeoForge recommends Forge for that release. The NeoForge profiles pin the exact published loader build, including upstream beta builds where no stable build exists.

Every example artifact embeds the targeted game release in its filename, declares that exact Minecraft version in mod metadata, and includes the portable runtime classes so the example can run as a standalone client mod. CI also verifies the runtime entry-point class is present in each JAR. The source matrix and CI results are the authority for what has compiled; rows are not claims of manual in-game testing.

## Minecraft 26.1–26.3

Minecraft 26 is unobfuscated and requires Java 25. Fabric uses Loom's non-remapping plugin ID with Mojang's class names; Forge uses ForgeGradle 7; NeoForge uses ModDevGradle. The GUI pipeline also changed from `GuiGraphics`/`DrawContext` rendering to `GuiGraphicsExtractor` plus `extractRenderState`, with event-object input callbacks. These are source and build-pipeline changes, not a safe version-range bump. Never select a 26.x artifact on a 1.20–1.21 instance.

| Loader | Exact Minecraft releases in CI | Java | Toolchain / mappings |
|---|---|---:|---|
| Fabric | 26.1, 26.1.1, 26.1.2, 26.2, 26.3 | 25 | Fabric Loom, unobfuscated Mojang names |
| Forge | 26.1, 26.1.1, 26.1.2, 26.2, 26.3 | 25 | ForgeGradle 7, unobfuscated Mojang names |
| NeoForge | 26.1 (beta), 26.1.1 (beta), 26.1.2, 26.2 | 25 | ModDevGradle, unobfuscated Mojang names |

As of 2026-09-23, NeoForge's official Maven repository has no 26.3 development artifact, so there is no 26.3 NeoForge build row. NeoForge's 26.1 and 26.1.1 rows use the published beta loader builds; stable artifacts are available for 26.1.2 and 26.2. The exact pinned coordinates live in `.github/workflows/build-loaders-26.yml`.

## Build

Run an individual classic baseline locally with `npm run build:example-fabric`, `npm run build:example-forge`, or `npm run build:example-neoforge`. The pinned classic and 26.x matrices run in `.github/workflows/build-loaders.yml` and `.github/workflows/build-loaders-26.yml`; CI is used for cross-version builds because Gradle cannot establish its loopback daemon connection in some Windows environments.
