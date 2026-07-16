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
npm install github:dragoscimpean/cimbyte-ui-kit#v0.2.1
```

Import the styles and, when the app uses interactive data attributes, the optional behavior layer:

```js
import "@cimbyte/ui/css";
import "@cimbyte/ui";
```

The CSS has no framework or runtime dependency. Plain HTML and desktop web shells can also load the release through jsDelivr:

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/dragoscimpean/cimbyte-ui-kit@v0.2.1/cimbyte.css">
<script src="https://cdn.jsdelivr.net/gh/dragoscimpean/cimbyte-ui-kit@v0.2.1/cimbyte.js" defer></script>
```

Then use the `cb-` classes directly:

```html
<button class="cb-btn cb-btn-accent">Save</button>
<section class="cb-card">...</section>
```

Dark mode is the default. Set `<html data-cb-theme="light">` for light mode, or override semantic CSS variables such as `--cb-accent`, `--cb-bg`, and `--cb-ink` at an application boundary.

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
    implementation("com.github.dragoscimpean:cimbyte-ui-kit:v0.2.1")
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
