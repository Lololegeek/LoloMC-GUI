# LoloMC GUI 0.2.1

## Distribution and compatibility

- Bundle the portable LoloMC GUI runtime into each loader example JAR so it can open its sample screen without a separate runtime file.
- Publish separate CI-built example artifacts per Minecraft version and loader instead of implying that one Minecraft-specific JAR works everywhere.
- Verify in CI that each output JAR actually contains `dev.lolomc.gui.LoloGui`.
- Cover Fabric, Forge, and NeoForge from Minecraft 1.20.1 onward wherever upstream development artifacts are available, including the dedicated Minecraft 26.x build pipeline.
- Document the Forge gaps at 1.20.5 and 1.21.2. NeoForge 26.3 became available upstream after the initial 0.2.1 matrix; see the current version matrix for its build.

The compatibility matrix is build-verified by GitHub Actions. Not every version/loader combination has been manually launched in-game.
