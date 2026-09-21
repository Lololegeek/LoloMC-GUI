# XML/CSS reference

LoloMC GUI uses a deliberately small, stable, predictable dialect. XML describes the semantic tree; CSS controls the visual result.

## Elements

| Element | Usage |
|---|---|
| `screen` | Single document root |
| `panel` | Generic flex container |
| `label` | Text |
| `button` | Clickable action with `on-click` |
| `input` | Text field with `value`, `placeholder`, and `on-change` |
| `image` | Texture identified by `src` |
| `badge`, `avatar` | Small visual components |
| `progress` | Progress bar driven by `value`, `max`, and `progress-color` |
| `spacer` | Flexible space, usually `flex: 1` |

Every element accepts `id`, `class`, and `style`. Actions stay in Java code; XML never contains scripts.

## Selectors

The runtime supports element selectors (`button`), classes (`.primary`), IDs (`#join`), combinations (`button.primary`), descendants (`.toolbar button`), comma-separated lists, and `:hover`, `:active`, and `:focus` states. The cascade follows specificity and then source order.

## Properties

- Layout: `display`, `flex-direction`, `flex`, `width`, `height`, `min-width`, `max-width`, `padding`, `margin`, `gap`, `justify-content`, `align-items`, `align-self`, `position`, `left`, `right`, `top`, `bottom`.
- Appearance: `background`, `linear-gradient(...)`, `color`, `opacity`, `box-shadow`, `border-width`, `border-color`, `border-radius`, `overflow`, `tint`.
- Text: `font-size`, `line-height`, `letter-spacing`, `font-family`, `text-align`, `vertical-align`, `text-transform`, `text-shadow`, `placeholder-color`.
- Values: unitless pixels or `px`, percentages for `width`/`height`, `#RGB`, `#RRGGBB`, `#RRGGBBAA`, `rgb(...)`, and `rgba(...)` colors.
- Global variables: declare `--accent` in `:root`, then use `var(--accent)`.

This is not a browser: grid, CSS animations, `calc()`, and the child combinator `>` are not part of version 0.1. Gradients are intentionally linear, and shadows are rendered by the Minecraft adapter to stay compatible with older versions.
