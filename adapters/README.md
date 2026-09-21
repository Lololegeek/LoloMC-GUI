# Adaptateurs Minecraft

Ces recettes sont le raccord concret entre `MinecraftScreenHost` et les classes `Screen` de Minecraft.

- `fabric-yarn-1.20-1.21` : Fabric ou Quilt avec mappings Yarn et `DrawContext`.
- `forge-neoforge-mojmap-1.20-1.21` : Forge ou NeoForge avec Mojmap et `GuiGraphics`.

Copiez les deux templates de votre famille dans le source-set client, remplacez le package, puis ouvrez l’écran normalement :

```java
MinecraftClient.getInstance().setScreen(new LoloScreen(session)); // Yarn
Minecraft.getInstance().setScreen(new LoloScreen(session));       // Mojmap
```

Les méthodes de texture ont volontairement un point d’intégration : leurs signatures sont celles qui changent le plus souvent, et les mods ont fréquemment leur propre atlas/cache. Le texte, les rectangles, les bordures, le clipping et toutes les entrées sont déjà raccordés.

Pour 1.12–1.19, conservez `MinecraftScreenHost` et remplacez seulement les imports/classes graphiques par `GuiScreen`, `MatrixStack` ou `PoseStack`. Le runtime et les fichiers XML/CSS ne changent pas.

