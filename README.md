# Cimbyte UI

Standalone CSS + JS design system. Framework-agnostic — works in React, Svelte, or plain HTML. No build step, no dependencies. Ported from the Auditore design language (OKLCH dark theme, Inter Tight + JetBrains Mono).

## Install

Private package — install straight from the git repo (no registry needed), pinned to a tag:

```bash
npm install git+ssh://git@github.com/cimbyte/cimbyte-ui-kit.git#v0.1.0
```

Then in your app entry:

```js
import "@cimbyte/ui/css";   // styles
import "@cimbyte/ui";        // optional interactive behaviors (window.Cimbyte)
```

Or drop the files in directly (plain HTML, no bundler):

```html
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter+Tight:wght@400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
<link rel="stylesheet" href="cimbyte.css">
<script src="cimbyte.js" defer></script>
```

Then just use classes — no setup, no build:

```html
<button class="cb-btn cb-btn-primary">Search</button>
<div class="cb-card">…</div>
```

## Interactive behaviors (data-attributes)

| What | Markup |
|---|---|
| Dropdown | `<button data-cb-toggle="dropdown">` + sibling `.cb-menu` |
| Modal | `data-cb-toggle="modal" data-cb-target="#id"`, dismiss with `data-cb-dismiss="modal"` |
| Drawer | `data-cb-toggle="drawer" data-cb-target="#id"` |
| Tabs | `.cb-tab[data-cb-tab="#panelId"]` + `.cb-tab-panel#panelId` |
| Sort table | `<table class="cb-table" data-cb-sortable>` + `th.is-sortable` |
| Toast | `Cimbyte.toast("Saved", { type: "success" })` |

## Theme

Dark by default. Light theme: `<html data-cb-theme="light">`. Override any token via CSS variables (`--cb-accent`, `--cb-bg`, `--cb-ink`, …).

## Catalog

Open `demo/index.html` (or `npm run demo` → http://localhost:4180/demo/) — every component, also serves as the visual test.

## Files

- `cimbyte.css` — tokens + all component styles (the only required file)
- `cimbyte.js` — optional vanilla behaviors for interactive components
