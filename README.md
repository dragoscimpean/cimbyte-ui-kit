# Cimbyte UI

Cimbyte UI is the shared design system for Cimbyte products. It ships two native artifacts from one set of design tokens:

| Target | Artifact | Best for |
|---|---|---|
| Web and desktop web shells | `@cimbyte/ui` CSS + optional vanilla JS | Browser apps, React, Svelte, Electron, and Tauri |
| Android | `cimbyte-ui-compose` AAR | Native Jetpack Compose apps |

The artifacts share color, spacing, radius, typography, and control-size decisions. Components are implemented natively for each platform: Android does not embed the web catalog in a WebView.

## Web installation

Install the public repository and pin it to a release:

```bash
npm install github:dragoscimpean/cimbyte-ui-kit#v0.3.0
```

Import the styles and, when the app uses interactive data attributes, the optional behavior layer:

```js
import "@cimbyte/ui/css";
import "@cimbyte/ui";
```

The CSS has no framework or runtime dependency. Plain HTML and desktop web shells can also load the release through jsDelivr:

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/dragoscimpean/cimbyte-ui-kit@v0.3.0/cimbyte.css">
<script src="https://cdn.jsdelivr.net/gh/dragoscimpean/cimbyte-ui-kit@v0.3.0/cimbyte.js" defer></script>
```

Then use the `cb-` classes directly:

```html
<button class="cb-btn cb-btn-accent">Save</button>
<section class="cb-card">...</section>
```

Dark mode is the default. Set `<html data-cb-theme="light">` for light mode, or override semantic CSS variables such as `--cb-accent`, `--cb-bg`, and `--cb-ink` at an application boundary.

Add `cb-blueprint` to a page or scrollable application surface for the reusable dotted workspace background. Authentication shells include the same background automatically.

## Android installation

Public Android releases are available through JitPack. Add its repository in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

Add the Compose library:

```kotlin
dependencies {
    implementation("com.github.dragoscimpean:cimbyte-ui-kit:v0.3.0")
}
```

Use the native theme and components:

```kotlin
import ro.cimbyte.ui.CimbyteButton
import ro.cimbyte.ui.CimbyteButtonVariant
import ro.cimbyte.ui.CimbyteCard
import ro.cimbyte.ui.CimbyteSectionHeader
import ro.cimbyte.ui.CimbyteTheme

CimbyteTheme {
    CimbyteCard {
        CimbyteSectionHeader(
            title = "Workspace",
            subtitle = "Native Android UI",
        )
        CimbyteButton(
            onClick = ::save,
            variant = CimbyteButtonVariant.Accent,
        ) {
            Text("Save")
        }
    }
}
```

The Android public API also includes badges, empty and loading states, settings rows, and code blocks. `CimbyteTheme.colors`, `CimbyteTheme.dimensions`, `CimbyteTheme.typography`, `CimbyteFonts.sans`, and `CimbyteFonts.mono` expose the shared visual language to application-specific composables.

## Component catalog

Run `npm run demo`, then open [http://localhost:4180/demo/](http://localhost:4180/demo/). The catalog covers:

- Buttons, form controls, comboboxes, token inputs, badges, pills, alerts, cards, stats, identity cells, status indicators, and meters
- Data tables and dense grids, trees, tabs, document tabs, toolbars, status bars, find bars, inspector panes, split views, master/detail layouts, and responsive workspace shells
- Settings and disclosure rows, selectable cards, bulk actions, drop zones, upload rows, empty/loading states, authentication layouts, code/diff blocks, and media controls
- Accessible keyboard behavior for dropdowns, dialogs, drawers, tabs, pills, sortable tables, selectable rows, and toasts

The generic visual and interaction patterns found in Auditore, Intelligence v2, Dbzator, and Nomadic belong here. Product workflows, domain rules, data access, editors, and provider integrations remain in their applications.

## Web composition contracts

Application shells compose `cb-app`, `cb-sidebar`, `cb-main`, and `cb-main-scroll`. Use `cb-page cb-page-contained` for a centered content area and set `--cb-page-max` only when a product needs a different maximum width. Set `--cb-sidebar-width` when the default 260px sidebar is not appropriate. Sidebar navigation can use `cb-sidebar-header`, `cb-sidebar-nav`, `cb-sidebar-group`, `cb-sidebar-footer`, and `cb-sidebar-close`; pair the in-sidebar close control with the fixed `cb-sidebar-toggle`. The application remains responsible for its navigation state.

Use `cb-section-head`, `cb-section-head-main`, `cb-section-title`, `cb-section-copy`, and `cb-section-actions` for headings inside cards or standalone page sections. `cb-grid-2`, `cb-grid-3`, `cb-grid-4`, and `cb-grid-auto` provide common columns; add `cb-grid-responsive` when the grid should collapse to one column on compact screens. `cb-stat-grid` and `cb-kv-list` cover metric cards and wrapped `dt`/`dd` metadata rows without product-specific selectors.

Responsive tables are opt-in. Add `cb-table-wrap-responsive` to the existing `cb-table-wrap`, make the wrapper keyboard-focusable and label it as a region, and add a `data-label` value to every body cell:

```html
<div class="cb-table-wrap cb-table-wrap-responsive" tabindex="0" role="region" aria-label="Companies">
  <table class="cb-table">
    <caption class="cb-sr-only">Companies and current status</caption>
    <thead><tr><th>Company</th><th>Status</th></tr></thead>
    <tbody><tr><td data-label="Company">Example SRL</td><td data-label="Status">Active</td></tr></tbody>
  </table>
</div>
```

The compact layout uses those labels below 640px. Keep colspan-heavy or custom grid tables in the standard horizontally scrolling wrapper. Sorting, filtering, and pagination remain application responsibilities.

Drawers require a `cb-scrim` immediately before the `cb-drawer` so the optional behavior layer can open and close both elements together. Use `cb-drawer-header`, `cb-drawer-title`, `cb-drawer-body`, `cb-drawer-section`, and `cb-drawer-footer` for its anatomy. Set `--cb-drawer-width` on the drawer when the default 440px width is not appropriate. The behavior layer supplies dialog semantics, title association, focus trapping, Escape dismissal, and focus restoration:

```html
<div class="cb-scrim"></div>
<aside class="cb-drawer" id="details">
  <header class="cb-drawer-header"><h2 class="cb-drawer-title">Details</h2></header>
  <div class="cb-drawer-body"><section class="cb-drawer-section">...</section></div>
</aside>
```

## Design tokens

[`tokens/cimbyte.tokens.json`](tokens/cimbyte.tokens.json) is the canonical source for both platforms. Generated CSS variables live at the top of `cimbyte.css`; generated Compose values live in `GeneratedCimbyteTokens.kt`.

```bash
npm run generate        # regenerate both outputs
npm run generate:check  # fail when generated files have drifted
```

Change semantic tokens rather than editing either generated block. Application-specific composition can use the tokens without adding every application component to the kit.

## Development and release checks

Requirements for Android work are JDK 17, Android SDK 35, and Kotlin/Compose compiler 2.0.21.

```bash
npm run check
npm run android:assemble
npm pack --dry-run
```

Pushing a `v*` tag verifies both artifacts and creates a GitHub release containing the web tarball and Android AAR. See [`CONTRIBUTING.md`](CONTRIBUTING.md) for the change boundary and verification workflow.

## License

MIT

Bundled Android fonts retain their original licenses in [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md).
