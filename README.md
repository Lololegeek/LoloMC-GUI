# LoloMC GUI

**Des interfaces Minecraft modernes en XML + CSS, sans enfermer votre mod dans un loader ou une version.**

LoloMC GUI réunit un runtime Java léger et **LoloMC GUI Studio**, un éditeur visuel dédié. Le cœur parse, style, met en page et anime l’état de l’interface ; votre mod ne traduit que six primitives vers l’API graphique de sa version de Minecraft.

## Ce qui fonctionne déjà

- XML déclaratif sécurisé : `screen`, `panel`, `label`, `button`, `input`, `image`, `badge`, `avatar`, `spacer`.
- CSS spécialisé : cascade, classes, identifiants, descendants, variables, héritage, `:hover`, `:active`, `:focus`.
- Layout flex ligne/colonne responsive, centrage, alignements, marges, tailles fixes ou en pourcentage, `flex`, position absolue, padding et gap.
- Rendu premium portable : gradients linéaires, ombres, coins arrondis, opacité, couleurs RGBA, texte aligné/ombré, barre `progress`.
- Clics, actions nommées, focus, saisie et suppression dans les champs texte.
- Backend de rendu indépendant de Fabric, Quilt, Forge, NeoForge et des mappings.
- Studio avec aperçu interactif, palette, arbre, inspecteur, XML/CSS, undo/redo, import et export ZIP.
- Bytecode Java 8, sans dépendance runtime externe.

## Essai en 60 secondes

Prérequis : JDK 21 (pour construire le bytecode Java 8) et Node.js 20+.

```powershell
npm install
npm run check
npm run dev -w studio
```

Le JAR est généré dans `runtime-java/build/lolomc-gui-0.1.0.jar`. Le Studio de production est dans `studio/dist/`.

## Premier écran

```xml
<screen id="hello" class="page">
  <panel class="card">
    <label class="title">Mon super mod</label>
    <input id="name" placeholder="Votre pseudo" on-change="rename" />
    <button class="primary" on-click="play">Jouer</button>
  </panel>
</screen>
```

```css
:root { --accent: #e9a23b; }
screen { padding: 24; background: #0d0f12; color: #f4f1ea; }
.card { width: 320; height: 180; padding: 18; gap: 10; background: #181b20; border-radius: 9; }
.title { height: 28; font-size: 20; }
input { height: 34; padding: 8; background: #242830; border-radius: 5; }
.primary { width: 120; height: 36; background: var(--accent); border-radius: 6; }
.primary:hover { background: #ffc261; }
```

```java
GuiSession ui = LoloGui.session(LoloGui.load(xmlStream, cssText))
    .on("play", node -> startGame())
    .on("rename", node -> setName(node.attr("value", "")));

ui.resize(width, height);
ui.render(renderBackend);
```

## Structure

```text
runtime-java/  moteur Java 8 portable et sans dépendance
studio/        éditeur visuel React + TypeScript
adapters/      écrans copiables Fabric/Quilt et Forge/NeoForge
examples/      écrans prêts à ouvrir et exemple Java
examples/fabric-mod/ mod Fabric jouable, touche O, ressources XML/CSS
docs/          intégration loaders, compatibilité et référence XML/CSS
```

Commencez par [le guide d’intégration](docs/integration.md), consultez [la référence XML/CSS](docs/xml-css-reference.md), puis lisez [la matrice de compatibilité](docs/compatibility.md). L’exemple `server-selector` est directement importable dans le Studio.

Pour voir la bibliothèque dans une vraie GUI Minecraft, ouvrez [l’exemple Fabric](examples/fabric-mod/README.md), puis lancez `npm run build:example-fabric` et `npm run run:example-fabric`. Dans le client de développement, la touche **O** ouvre l’écran XML/CSS.

## Philosophie de compatibilité

Minecraft change ses noms de classes et ses signatures graphiques selon les versions et mappings. LoloMC GUI ne prétend pas qu’un mixin compilé une fois peut ignorer cette réalité : il stabilise toute la partie complexe (document, styles, layout, état, saisie) derrière `RenderBackend`. Les adaptateurs par famille de versions restent petits, explicites et remplaçables.

## Statut

Version `0.1.0` : fondation fonctionnelle destinée aux premiers mods et retours d’intégration. Les propriétés prises en charge sont volontairement documentées ; ce moteur n’essaie pas d’embarquer un navigateur complet dans Minecraft.

Licence [MIT](LICENSE).
