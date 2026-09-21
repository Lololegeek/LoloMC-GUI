# LoloMC GUI

**Beautiful, data-driven Minecraft interfaces built with XML and CSS.**

LoloMC GUI is a loader-neutral Java runtime plus **LoloMC GUI Studio**, a visual editor for building polished in-game screens. Mods provide a tiny rendering adapter; the document, style cascade, layout, state and input handling stay portable.

## What works today

- Secure declarative XML: `screen`, `panel`, `label`, `button`, `input`, `image`, `badge`, `avatar`, `spacer`, and `progress`.
- CSS-like selectors: elements, classes, IDs, descendants, variables, inheritance, `:hover`, `:active`, and `:focus`.
- Responsive flex layout with centering, alignment, margins, fixed or percentage sizes, `flex`, absolute positioning, padding, gaps, and min/max bounds.
- Premium portable rendering: linear gradients, shadows, rounded corners, opacity, RGBA colors, aligned/shadowed text, and progress bars.
- Click actions, named callbacks, focus, text input, and backspace handling.
- Rendering backend independent from Fabric, Quilt, Forge, NeoForge, and mappings.
- Studio with interactive preview, palette, document tree, inspector, XML/CSS editing, undo/redo, import, drag positioning, and ZIP export.
- Java 8 bytecode with no runtime dependency.

## 60-second setup

Requirements: JDK 21 (to build Java 8 bytecode) and Node.js 20+.

```powershell
npm install
npm run check
npm run dev -w studio
```

The runtime JAR is generated at `runtime-java/build/lolomc-gui-0.1.0.jar`. The production Studio build is written to `studio/dist/`.

## Your first screen

```xml
<screen id="hello" class="page">
  <panel class="card">
    <label class="title">My awesome mod</label>
    <input id="name" placeholder="Your name" on-change="rename" />
    <button class="primary" on-click="play">Play</button>
  </panel>
</screen>
```

```css
:root { --accent: #e9a23b; }
screen { padding: 24; background: #0d0f12; color: #f4f1ea; justify-content: center; align-items: center; }
.card { width: 86%; max-width: 420; height: 180; padding: 18; gap: 10; background: #181b20; border-radius: 9; box-shadow: 0 8 24 #00000080; }
.title { height: 28; font-size: 20; }
input { height: 34; padding: 8; background: #242830; border-radius: 5; }
.primary { width: 120; height: 36; background: linear-gradient(90deg, #e9a23b, #ffc261); border-radius: 6; }
```

```java
GuiSession ui = LoloGui.session(LoloGui.load(xmlStream, cssText))
    .on("play", node -> startGame())
    .on("rename", node -> setName(node.attr("value", "")));

ui.resize(width, height);
ui.render(renderBackend);
```

## Repository layout

```text
runtime-java/  portable Java 8 runtime
studio/        React + TypeScript visual editor
adapters/      copy-ready Fabric/Quilt and Forge/NeoForge templates
examples/      Fabric, Forge, and NeoForge runnable client examples
docs/          loader integration, compatibility, and XML/CSS reference
```

Start with the [integration guide](docs/integration.md), read the [XML/CSS reference](docs/xml-css-reference.md), then check the [compatibility matrix](docs/compatibility.md). The `server-selector` example can be imported directly into Studio.

To see the library inside a real Minecraft GUI, open one of the loader examples, then run its Gradle build. The root scripts are `npm run build:example-fabric`, `npm run build:example-forge`, `npm run build:example-neoforge`, or `npm run build:loader-matrix`. Press **O** in the development client to open the XML/CSS screen.

## Compatibility philosophy

Minecraft changes class names and rendering signatures across versions and mappings. LoloMC GUI keeps the document, styles, layout, state, and input universal behind `RenderBackend`; each version family only needs a small explicit adapter.

## Status

Version `0.1.0` is a functional foundation for early mod integrations and feedback. Supported properties are intentionally documented; this runtime is not trying to embed a full web browser inside Minecraft.

## License

MIT. See [LICENSE](LICENSE).
