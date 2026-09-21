# Intégrer LoloMC GUI dans un mod

## 1. Ajouter le runtime

Publiez `runtime-java/build/lolomc-gui-0.1.0.jar` dans votre dépôt Maven local ou placez-le dans `libs/`, puis utilisez `implementation(files("libs/lolomc-gui-0.1.0.jar"))`. Aucun loader n’est une dépendance transitive.

Placez `screen.xml` et `screen.css` dans `src/main/resources/assets/<modid>/ui/`.

## 2. Créer la session

```java
GuiSession ui = LoloGui.session(LoloGui.load(xmlStream, cssText))
    .on("joinServer", node -> connect())
    .on("filterServers", node -> filter(node.attr("value", "")));
```

À l’initialisation/redimensionnement de l’écran :

```java
ui.resize(screenWidth, screenHeight);
```

## 3. Relayer le cycle Minecraft

```java
ui.render(myBackend);
ui.mouseMoved(mouseX, mouseY);
ui.mouseClicked(mouseX, mouseY, button);
ui.mouseReleased();
ui.charTyped(character);
ui.keyPressed(keyCode); // 259 = retour arrière dans les mappings modernes
```

Votre `RenderBackend` traduit `fill`, `text` et `image` vers `GuiGraphics` (Mojmap), `DrawContext` (Yarn), `PoseStack` ou l’API de la version ciblée. Les bordures ont déjà une implémentation par défaut ; le clipping est optionnel. Cette classe courte est le seul code dépendant des mappings.

Des recettes copiables sont fournies dans [`adapters/`](../adapters/README.md) pour Yarn et Mojmap modernes. Elles s’appuient sur `MinecraftScreenHost`, qui relaie tout le cycle `Screen` sans dépendre du loader.

## 4. Workflow Studio

Lancez `npm run dev -w studio`, concevez l’écran, puis cliquez **Exporter**. Le ZIP contient `screen.xml` et `screen.css` prêts à copier dans les ressources du mod.
