# LoloMC GUI — mod Fabric exemple

Ce projet ouvre une vraie interface Minecraft avec la touche **O**.

Le mod charge :

- `src/main/resources/assets/lolomc_gui_example/ui/screen.xml`
- `src/main/resources/assets/lolomc_gui_example/ui/screen.css`

Puis il crée `GuiSession`, branche les actions `play`, `nameChanged` et `close`, et délègue le cycle `Screen` à `MinecraftScreenHost`.

## Compiler

Depuis ce dossier :

```powershell
gradlew.bat build
```

Le runtime parent `runtime-java` est consommé automatiquement via `includeBuild`. Le JAR final se trouve dans `build/libs/`.

## Lancer Minecraft de développement

```powershell
gradlew.bat runClient
```

Ce module cible Fabric/Minecraft 1.21.1 avec les mappings Yarn ; les APIs de rendu changent entre versions, mais les fichiers XML/CSS et `GuiSession` restent identiques.
