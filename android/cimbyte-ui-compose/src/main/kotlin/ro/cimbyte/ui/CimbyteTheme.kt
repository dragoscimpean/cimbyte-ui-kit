package ro.cimbyte.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

@Immutable
public data class CimbyteColors(
    val accent: Color,
    val onAccent: Color,
    val accentSoft: Color,
    val background: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val border: Color,
    val borderSoft: Color,
    val error: Color,
    val errorSoft: Color,
    val success: Color,
    val successSoft: Color,
    val warning: Color,
    val warningSoft: Color,
    val info: Color,
    val infoSoft: Color,
)

@Immutable
public data class CimbyteDimensions(
    val radiusSmall: Dp,
    val radiusMedium: Dp,
    val radiusLarge: Dp,
    val radiusExtraLarge: Dp,
    val radiusPill: Dp,
    val spacingExtraSmall: Dp,
    val spacingSmall: Dp,
    val spacingMedium: Dp,
    val spacingLarge: Dp,
    val spacingExtraLarge: Dp,
    val controlHeightSmall: Dp,
    val controlHeightMedium: Dp,
    val controlHeightLarge: Dp,
)

public object CimbyteFonts {
    public val sans: FontFamily = FontFamily(
        Font(R.font.cimbyte_inter_tight_regular, FontWeight.Normal),
        Font(R.font.cimbyte_inter_tight_medium, FontWeight.Medium),
        Font(R.font.cimbyte_inter_tight_semibold, FontWeight.SemiBold),
        Font(R.font.cimbyte_inter_tight_bold, FontWeight.Bold),
    )

    public val mono: FontFamily = FontFamily(
        Font(R.font.cimbyte_jetbrains_mono_regular, FontWeight.Normal),
    )
}

private val DarkColors = CimbyteColors(
    accent = CimbyteDarkColorTokens.accent,
    onAccent = CimbyteDarkColorTokens.onAccent,
    accentSoft = CimbyteDarkColorTokens.accentSoft,
    background = CimbyteDarkColorTokens.background,
    backgroundSecondary = CimbyteDarkColorTokens.backgroundSecondary,
    backgroundTertiary = CimbyteDarkColorTokens.backgroundTertiary,
    surface = CimbyteDarkColorTokens.surface,
    textPrimary = CimbyteDarkColorTokens.textPrimary,
    textSecondary = CimbyteDarkColorTokens.textSecondary,
    textMuted = CimbyteDarkColorTokens.textMuted,
    border = CimbyteDarkColorTokens.border,
    borderSoft = CimbyteDarkColorTokens.borderSoft,
    error = CimbyteDarkColorTokens.error,
    errorSoft = CimbyteDarkColorTokens.errorSoft,
    success = CimbyteDarkColorTokens.success,
    successSoft = CimbyteDarkColorTokens.successSoft,
    warning = CimbyteDarkColorTokens.warning,
    warningSoft = CimbyteDarkColorTokens.warningSoft,
    info = CimbyteDarkColorTokens.info,
    infoSoft = CimbyteDarkColorTokens.infoSoft,
)

private val LightColors = CimbyteColors(
    accent = CimbyteLightColorTokens.accent,
    onAccent = CimbyteLightColorTokens.onAccent,
    accentSoft = CimbyteLightColorTokens.accentSoft,
    background = CimbyteLightColorTokens.background,
    backgroundSecondary = CimbyteLightColorTokens.backgroundSecondary,
    backgroundTertiary = CimbyteLightColorTokens.backgroundTertiary,
    surface = CimbyteLightColorTokens.surface,
    textPrimary = CimbyteLightColorTokens.textPrimary,
    textSecondary = CimbyteLightColorTokens.textSecondary,
    textMuted = CimbyteLightColorTokens.textMuted,
    border = CimbyteLightColorTokens.border,
    borderSoft = CimbyteLightColorTokens.borderSoft,
    error = CimbyteLightColorTokens.error,
    errorSoft = CimbyteLightColorTokens.errorSoft,
    success = CimbyteLightColorTokens.success,
    successSoft = CimbyteLightColorTokens.successSoft,
    warning = CimbyteLightColorTokens.warning,
    warningSoft = CimbyteLightColorTokens.warningSoft,
    info = CimbyteLightColorTokens.info,
    infoSoft = CimbyteLightColorTokens.infoSoft,
)

private val DefaultDimensions = CimbyteDimensions(
    radiusSmall = CimbyteDefaultDimensions.radiusSmall,
    radiusMedium = CimbyteDefaultDimensions.radiusMedium,
    radiusLarge = CimbyteDefaultDimensions.radiusLarge,
    radiusExtraLarge = CimbyteDefaultDimensions.radiusExtraLarge,
    radiusPill = CimbyteDefaultDimensions.radiusPill,
    spacingExtraSmall = CimbyteDefaultDimensions.spacingExtraSmall,
    spacingSmall = CimbyteDefaultDimensions.spacingSmall,
    spacingMedium = CimbyteDefaultDimensions.spacingMedium,
    spacingLarge = CimbyteDefaultDimensions.spacingLarge,
    spacingExtraLarge = CimbyteDefaultDimensions.spacingExtraLarge,
    controlHeightSmall = CimbyteDefaultDimensions.controlHeightSmall,
    controlHeightMedium = CimbyteDefaultDimensions.controlHeightMedium,
    controlHeightLarge = CimbyteDefaultDimensions.controlHeightLarge,
)

private val LocalCimbyteColors = staticCompositionLocalOf { DarkColors }
private val LocalCimbyteDimensions = staticCompositionLocalOf { DefaultDimensions }

private val CimbyteTypography: Typography = Typography().let { defaults ->
    defaults.copy(
        displayLarge = defaults.displayLarge.copy(fontFamily = CimbyteFonts.sans),
        displayMedium = defaults.displayMedium.copy(fontFamily = CimbyteFonts.sans),
        displaySmall = defaults.displaySmall.copy(fontFamily = CimbyteFonts.sans),
        headlineLarge = defaults.headlineLarge.copy(fontFamily = CimbyteFonts.sans),
        headlineMedium = defaults.headlineMedium.copy(fontFamily = CimbyteFonts.sans),
        headlineSmall = defaults.headlineSmall.copy(fontFamily = CimbyteFonts.sans),
        titleLarge = defaults.titleLarge.copy(fontFamily = CimbyteFonts.sans, fontWeight = FontWeight.SemiBold),
        titleMedium = defaults.titleMedium.copy(fontFamily = CimbyteFonts.sans, fontWeight = FontWeight.SemiBold),
        titleSmall = defaults.titleSmall.copy(fontFamily = CimbyteFonts.sans, fontWeight = FontWeight.SemiBold),
        bodyLarge = defaults.bodyLarge.copy(fontFamily = CimbyteFonts.sans),
        bodyMedium = defaults.bodyMedium.copy(fontFamily = CimbyteFonts.sans),
        bodySmall = defaults.bodySmall.copy(fontFamily = CimbyteFonts.sans),
        labelLarge = defaults.labelLarge.copy(fontFamily = CimbyteFonts.sans, fontWeight = FontWeight.SemiBold),
        labelMedium = defaults.labelMedium.copy(fontFamily = CimbyteFonts.sans),
        labelSmall = defaults.labelSmall.copy(fontFamily = CimbyteFonts.sans),
    )
}

public object CimbyteTheme {
    public val colors: CimbyteColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCimbyteColors.current

    public val dimensions: CimbyteDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalCimbyteDimensions.current

    public val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    @Composable
    public operator fun invoke(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit,
    ) {
        val colors = if (darkTheme) DarkColors else LightColors
        val colorScheme = if (darkTheme) {
            darkColorScheme(
                primary = colors.accent,
                onPrimary = colors.onAccent,
                primaryContainer = colors.accentSoft,
                onPrimaryContainer = colors.textPrimary,
                secondary = colors.info,
                onSecondary = colors.onAccent,
                background = colors.background,
                onBackground = colors.textPrimary,
                surface = colors.surface,
                onSurface = colors.textPrimary,
                surfaceVariant = colors.backgroundTertiary,
                onSurfaceVariant = colors.textSecondary,
                surfaceContainer = colors.backgroundSecondary,
                surfaceContainerHigh = colors.backgroundTertiary,
                outline = colors.border,
                outlineVariant = colors.borderSoft,
                error = colors.error,
                onError = colors.onAccent,
                errorContainer = colors.errorSoft,
                onErrorContainer = colors.error,
            )
        } else {
            lightColorScheme(
                primary = colors.accent,
                onPrimary = colors.onAccent,
                primaryContainer = colors.accentSoft,
                onPrimaryContainer = colors.textPrimary,
                secondary = colors.info,
                onSecondary = colors.onAccent,
                background = colors.background,
                onBackground = colors.textPrimary,
                surface = colors.surface,
                onSurface = colors.textPrimary,
                surfaceVariant = colors.backgroundTertiary,
                onSurfaceVariant = colors.textSecondary,
                surfaceContainer = colors.backgroundSecondary,
                surfaceContainerHigh = colors.backgroundTertiary,
                outline = colors.border,
                outlineVariant = colors.borderSoft,
                error = colors.error,
                onError = colors.onAccent,
                errorContainer = colors.errorSoft,
                onErrorContainer = colors.error,
            )
        }

        CompositionLocalProvider(
            LocalCimbyteColors provides colors,
            LocalCimbyteDimensions provides DefaultDimensions,
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = CimbyteTypography,
                content = content,
            )
        }
    }
}
