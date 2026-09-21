# Compatibilité

## Garantie du cœur 0.1

| Environnement | Statut | Pourquoi |
|---|---|---|
| Java 8 à 21+ | Compatible | bytecode compilé avec `--release 8`, API standard uniquement |
| Fabric / Quilt | Compatible via adaptateur | aucune dépendance loader, mixin ou mapping |
| Forge / NeoForge | Compatible via adaptateur | aucune dépendance loader, événement ou mapping |
| Minecraft 1.12.2 à 1.21.x | Architecture compatible | les appels de rendu propres à chaque famille sont isolés dans `RenderBackend` |
| Client vanilla sans loader | Possible | nécessite un point d’injection fourni par l’intégrateur |

La compatibilité ne signifie pas qu’un même mod JAR ciblant les classes obfusquées de Minecraft fonctionne sur toutes les versions. LoloMC GUI rend le **document, le style, le layout et les interactions** universels ; le mod fournit seulement les six primitives de rendu et relaie les événements de son écran.

L’exemple livré et sa validation de build ciblent Fabric/Minecraft 1.21.1. Il sert de référence exécutable pour les autres familles ; les APIs de rendu spécifiques doivent être adaptées selon les mappings.

## Familles d’adaptateurs conseillées

- 1.12.2–1.16.5 : backend basé sur `MatrixStack`/primitives historiques.
- 1.17–1.19.4 : backend `PoseStack` ou `MatrixStack` selon mappings.
- 1.20–1.21.x : backend `GuiGraphics` (Mojmap) ou `DrawContext` (Yarn).

Le runtime reste identique entre ces modules. Seul le fichier adaptateur change.
