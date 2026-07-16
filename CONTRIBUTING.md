# Contributing

## Design tokens

Edit `tokens/cimbyte.tokens.json`, then run:

```bash
npm run generate
```

The generator updates the token block in `cimbyte.css` and the Compose values in `GeneratedCimbyteTokens.kt`. Do not edit either generated section directly.

## Web UI

Keep web classes under the `cb-` prefix. Add reusable visual structure to `cimbyte.css`; keep product workflows in their applications. Add or update the matching example in `demo/index.html`.

Run the catalog locally with:

```bash
npm run demo
```

## Android UI

The Android library lives under `android/cimbyte-ui-compose`. Keep its public surface focused on theme, semantic tokens, and reusable native UI. Product states and application workflows do not belong in the library.

Build the release AAR with:

```bash
npm run android:assemble
```

## Verification

Run non-unit verification before publishing:

```bash
npm run generate
npm run check
npm pack --dry-run
npm run android:assemble
```

Review the web catalog in both themes and inspect the Compose previews before tagging a release.
