# Cimbyte UI for Android

`cimbyte-ui-compose` is the native Jetpack Compose implementation of Cimbyte UI. It shares generated design tokens with the web package and does not use a WebView.

## Requirements

- Android minSdk 23 and compileSdk 35
- JDK 17
- Kotlin and Compose compiler 2.0.21

## Public release

Add JitPack to the consumer's repositories:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

Then add the tagged release:

```kotlin
dependencies {
    implementation("com.github.dragoscimpean:cimbyte-ui-kit:v0.2.2")
}
```

## Local development

```bash
./gradlew :cimbyte-ui-compose:assembleRelease
./gradlew :cimbyte-ui-compose:publishToMavenLocal
```

The local coordinate is `ro.cimbyte.ui:cimbyte-ui-compose:0.2.2-SNAPSHOT`. Override the version with `-PCIMBYTE_VERSION=0.2.2`.

```kotlin
repositories {
    mavenLocal()
    google()
    mavenCentral()
}

dependencies {
    implementation("ro.cimbyte.ui:cimbyte-ui-compose:0.2.2-SNAPSHOT")
}
```

## Usage

```kotlin
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

Available primitives include `CimbyteCard`, `CimbyteBadge`, `CimbyteButton`, `CimbyteSectionHeader`, `CimbyteEmptyState`, `CimbyteLoadingState`, `CimbyteSettingsRow`, and `CimbyteCodeBlock`.

Use `CimbyteTheme.colors`, `CimbyteTheme.dimensions`, and `CimbyteTheme.typography` inside composables. `CimbyteFonts.sans` and `CimbyteFonts.mono` expose the bundled, resource-prefixed Inter Tight and JetBrains Mono font families.

`GeneratedCimbyteTokens.kt` is generated from the repository's canonical `tokens/cimbyte.tokens.json` and is not a public API.
