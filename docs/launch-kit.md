# LoloMC GUI launch kit

This file contains ready-to-publish English copy for the first public release. It is intentionally a draft: publishing to third-party communities still requires the account owner to review and post it.

## One-line pitch

LoloMC GUI is a loader-neutral Minecraft GUI library that lets mod authors build polished in-game interfaces with XML, CSS, and a visual editor.

## GitHub release notes

### LoloMC GUI 0.1.0

Build beautiful Minecraft interfaces without rebuilding every screen from scratch.

This first public release includes:

- A Java 8-compatible runtime for XML documents, CSS styling, responsive layout, state, and input handling.
- A small `RenderBackend` contract that keeps Minecraft-version-specific rendering code in adapters.
- LoloMC GUI Studio with an interactive preview, XML/CSS editing, drag positioning, undo/redo, import, and export.
- A runnable Fabric 1.21.1 example mod showing a responsive in-game screen.
- A server-selector example and compatibility documentation for Fabric, Forge, NeoForge, and server-side integrations.

The API is intentionally small and experimental in 0.1.0. Feedback, adapter contributions, and example screens are welcome.

## Modrinth project description

LoloMC GUI is a client-side GUI library for Minecraft mod authors. Define your screen structure in XML, style it with a focused CSS dialect, and render it through a tiny adapter for your target Minecraft version or loader.

The project is designed for beautiful, responsive in-game menus: server selectors, settings screens, dashboards, onboarding flows, and custom mod interfaces. LoloMC GUI Studio gives you a visual canvas to inspect the document tree, drag elements, tune styles, and export ready-to-use XML/CSS files.

### Highlights

- XML + CSS screens loaded at runtime.
- Responsive flex-style layout with percentages, min/max sizes, alignment, spacing, gradients, shadows, rounded borders, opacity, progress bars, and text styling.
- Loader-neutral Java runtime with explicit rendering adapters.
- Studio workflow with interactive preview, direct manipulation, undo/redo, and ZIP export.
- No mandatory dependency on a specific mod loader.

### Project status

This is an early public release. The core document, styling, layout, and interaction APIs are usable, while Minecraft adapters are version-family specific and should be tested against the exact game version you ship.

## Reddit post draft

**Title:** I built LoloMC GUI, an XML + CSS library for polished in-game Minecraft screens

**Body:**

I have been working on LoloMC GUI, a loader-neutral GUI library for Minecraft mod authors.

The idea is simple: describe the screen in XML, style it with CSS, and keep the Minecraft-specific rendering code inside a small adapter. The repository also includes LoloMC GUI Studio, a visual editor with an interactive preview, drag positioning, XML/CSS tabs, undo/redo, and export.

The first release includes a runnable Fabric 1.21.1 example with a responsive server-style screen. It is aimed at mod menus, server selectors, settings, onboarding, and other interfaces that should look more intentional than a pile of hard-coded rectangles.

GitHub: https://github.com/Lololegeek/LoloMC-GUI

I would love feedback on the API shape, the CSS subset, and which Minecraft version or loader should get the next adapter.

## Discord announcement

LoloMC GUI 0.1.0 is live! It is a loader-neutral Minecraft GUI library for XML + CSS screens, with a visual Studio editor and a Fabric example mod. Build responsive menus, server selectors, settings screens, and custom mod UI with a small adapter layer.

Repository: https://github.com/Lololegeek/LoloMC-GUI

## X / short social post

LoloMC GUI 0.1.0 is out: a loader-neutral Minecraft GUI library for polished XML + CSS screens, plus a visual editor with drag positioning and live preview. Fabric example included.

https://github.com/Lololegeek/LoloMC-GUI

## Posting checklist

- Confirm the GitHub repository is public and the release artifact downloads correctly.
- Create the Modrinth project with the same English description and link the GitHub repository.
- Attach the correct Minecraft versions and loader environments to each uploaded artifact.
- Review the Reddit title and subreddit rules before posting.
- Replace the links above if the hosting platform assigns a different canonical URL.
