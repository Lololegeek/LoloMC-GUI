# Référence XML/CSS

LoloMC GUI emploie un dialecte volontairement petit, stable et prévisible. Un fichier XML décrit la sémantique ; un fichier CSS contrôle tout le visuel.

## Éléments

| Élément | Usage |
|---|---|
| `screen` | Racine unique du document |
| `panel` | Conteneur flex générique |
| `label` | Texte |
| `button` | Action cliquable avec `on-click` |
| `input` | Champ texte avec `value`, `placeholder` et `on-change` |
| `image` | Texture identifiée par `src` |
| `badge`, `avatar` | Petits composants visuels |
| `progress` | Barre de progression pilotée par `value`, `max` et `progress-color` |
| `spacer` | Espace flexible, généralement `flex: 1` |

Tous les éléments acceptent `id`, `class` et `style`. Les actions restent dans le code Java : le XML ne contient jamais de script.

## Sélecteurs

Le runtime accepte les sélecteurs d’élément (`button`), de classe (`.primary`), d’identifiant (`#join`), combinés (`button.primary`), descendants (`.toolbar button`), listes séparées par une virgule et états `:hover`, `:active`, `:focus`. La cascade suit spécificité puis ordre source.

## Propriétés

- Layout : `display`, `flex-direction`, `flex`, `width`, `height`, `min-width`, `max-width`, `padding`, `margin`, `gap`, `justify-content`, `align-items`, `align-self`, `position`, `left`, `right`, `top`, `bottom`.
- Apparence : `background`, `linear-gradient(...)`, `color`, `opacity`, `box-shadow`, `border-width`, `border-color`, `border-radius`, `overflow`, `tint`.
- Texte : `font-size`, `line-height`, `letter-spacing`, `font-family`, `text-align`, `vertical-align`, `text-transform`, `text-shadow`, `placeholder-color`.
- Valeurs : pixels sans unité ou avec `px`, pourcentages sur `width`/`height`, couleurs `#RGB`, `#RRGGBB`, `#RRGGBBAA`, `rgb(...)` et `rgba(...)`.
- Variables globales : déclarer `--accent` dans `:root`, puis utiliser `var(--accent)`.

Ce n’est pas un navigateur : grid, animations CSS, `calc()` et combinateur enfant `>` ne font pas partie de la version 0.1. Les gradients sont volontairement linéaires et les ombres sont rendues par l’adaptateur Minecraft pour rester compatibles avec les anciennes versions.
