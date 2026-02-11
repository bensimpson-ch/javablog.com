# Style Guide

## SCSS Cascade

1. **`src/styles.scss`** — Global foundation. Design tokens (CSS custom properties), resets, base element styles (typography, links, code, lists, blockquotes). Applied to the entire document.
2. **`src/app/app.scss`** — Root component. Page shell layout (header, main, footer) and navigation. `<main>` sets the page-level width constraint (`max-width: var(--content-width)`).
3. **`src/app/pages/*/*.scss`** — Page components. Scoped to each view via Angular's ViewEncapsulation.

## Layout Contexts

Shell width (header nav, main, footer) is route-aware. The root component (`app.ts`) binds an `author-mode` class on `<app-root>` when the path starts with `/author`. `app.scss` responds with `:host(.author-mode)` to widen the shell.

- **Reader** (`--content-width: 700px`): Home list, post view. Default shell width.
- **Author** (`--content-width-wide: 1400px`): Post editor. Shell widens via `author-mode` class on `<app-root>`.
