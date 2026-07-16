package ro.cimbyte.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

public enum class CimbyteBadgeVariant {
    Neutral,
    Accent,
    Success,
    Warning,
    Error,
    Info,
}

public enum class CimbyteButtonVariant {
    Primary,
    Secondary,
    Accent,
    Ghost,
    Danger,
}

@Composable
public fun CimbyteCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(CimbyteTheme.dimensions.spacingLarge),
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = CimbyteTheme.colors
    Surface(
        modifier = modifier,
        color = colors.surface,
        contentColor = colors.textPrimary,
        shape = RoundedCornerShape(CimbyteTheme.dimensions.radiusLarge),
        border = BorderStroke(1.dp, colors.border),
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content,
        )
    }
}

@Composable
public fun CimbyteBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: CimbyteBadgeVariant = CimbyteBadgeVariant.Neutral,
    leadingContent: (@Composable () -> Unit)? = null,
) {
    val colors = CimbyteTheme.colors
    val containerColor: Color
    val contentColor: Color
    when (variant) {
        CimbyteBadgeVariant.Neutral -> {
            containerColor = colors.backgroundTertiary
            contentColor = colors.textSecondary
        }
        CimbyteBadgeVariant.Accent -> {
            containerColor = colors.accentSoft
            contentColor = colors.accent
        }
        CimbyteBadgeVariant.Success -> {
            containerColor = colors.successSoft
            contentColor = colors.success
        }
        CimbyteBadgeVariant.Warning -> {
            containerColor = colors.warningSoft
            contentColor = colors.warning
        }
        CimbyteBadgeVariant.Error -> {
            containerColor = colors.errorSoft
            contentColor = colors.error
        }
        CimbyteBadgeVariant.Info -> {
            containerColor = colors.infoSoft
            contentColor = colors.info
        }
    }

    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(CimbyteTheme.dimensions.radiusSmall),
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent?.invoke()
            Text(
                text = text,
                fontFamily = CimbyteFonts.mono,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.1.sp,
            )
        }
    }
}

@Composable
public fun CimbyteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: CimbyteButtonVariant = CimbyteButtonVariant.Primary,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = CimbyteTheme.colors
    val containerColor: Color
    val contentColor: Color
    val borderColor: Color?
    when (variant) {
        CimbyteButtonVariant.Primary -> {
            containerColor = colors.textPrimary
            contentColor = colors.background
            borderColor = colors.textPrimary
        }
        CimbyteButtonVariant.Secondary -> {
            containerColor = colors.surface
            contentColor = colors.textSecondary
            borderColor = colors.border
        }
        CimbyteButtonVariant.Accent -> {
            containerColor = colors.accent
            contentColor = colors.onAccent
            borderColor = colors.accent
        }
        CimbyteButtonVariant.Ghost -> {
            containerColor = Color.Transparent
            contentColor = colors.textSecondary
            borderColor = null
        }
        CimbyteButtonVariant.Danger -> {
            containerColor = colors.error
            contentColor = colors.onAccent
            borderColor = colors.error
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = CimbyteTheme.dimensions.controlHeightMedium),
        enabled = enabled,
        shape = RoundedCornerShape(CimbyteTheme.dimensions.radiusSmall),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.45f),
            disabledContentColor = contentColor.copy(alpha = 0.5f),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        border = borderColor?.let { BorderStroke(1.dp, it) },
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        content = content,
    )
}

@Composable
public fun CimbyteSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    val colors = CimbyteTheme.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingExtraSmall),
        ) {
            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = colors.textMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        trailingContent?.invoke(this)
    }
}

@Composable
public fun CimbyteEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val colors = CimbyteTheme.colors
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.surface,
        contentColor = colors.textPrimary,
        shape = RoundedCornerShape(CimbyteTheme.dimensions.radiusLarge),
        border = BorderStroke(1.dp, colors.border),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = CimbyteTheme.dimensions.spacingLarge,
                vertical = CimbyteTheme.dimensions.spacingExtraLarge,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall),
        ) {
            if (icon != null) {
                Surface(
                    color = colors.backgroundSecondary,
                    contentColor = colors.textMuted,
                    shape = RoundedCornerShape(CimbyteTheme.dimensions.radiusLarge),
                ) {
                    Row(modifier = Modifier.padding(CimbyteTheme.dimensions.spacingMedium)) {
                        icon()
                    }
                }
            }
            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium,
            )
            if (description != null) {
                Text(
                    text = description,
                    color = colors.textMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (action != null) {
                Row(modifier = Modifier.padding(top = CimbyteTheme.dimensions.spacingSmall)) {
                    action()
                }
            }
        }
    }
}

@Composable
public fun CimbyteSettingsRow(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    val colors = CimbyteTheme.colors
    val actionModifier = if (onClick == null) {
        Modifier
    } else {
        Modifier.clickable(role = Role.Button, onClick = onClick)
    }
    Row(
        modifier = modifier
            .then(actionModifier)
            .fillMaxWidth()
            .defaultMinSize(minHeight = CimbyteTheme.dimensions.controlHeightLarge)
            .padding(
                horizontal = CimbyteTheme.dimensions.spacingLarge,
                vertical = CimbyteTheme.dimensions.spacingMedium,
            ),
        horizontalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent?.invoke()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingExtraSmall),
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
            if (description != null) {
                Text(
                    text = description,
                    color = colors.textMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        trailingContent?.invoke(this)
    }
}

@Composable
public fun CimbyteCodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val colors = CimbyteTheme.colors
    Surface(
        modifier = modifier,
        color = colors.background,
        contentColor = colors.textSecondary,
        shape = RoundedCornerShape(CimbyteTheme.dimensions.radiusMedium),
        border = BorderStroke(1.dp, colors.border),
    ) {
        Column {
            if (label != null || action != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = CimbyteTheme.dimensions.spacingMedium,
                            vertical = CimbyteTheme.dimensions.spacingSmall,
                        ),
                    horizontalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (label != null) {
                        Text(
                            text = label,
                            modifier = Modifier.weight(1f),
                            color = colors.textMuted,
                            fontFamily = CimbyteFonts.mono,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    action?.invoke()
                }
            }
            Text(
                text = code,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(CimbyteTheme.dimensions.spacingMedium),
                color = colors.textSecondary,
                fontFamily = CimbyteFonts.mono,
                fontSize = 12.sp,
                lineHeight = 19.sp,
                softWrap = false,
            )
        }
    }
}

@Composable
public fun CimbyteLoadingState(
    modifier: Modifier = Modifier,
    label: String = "Loading",
) {
    Column(
        modifier = modifier.padding(CimbyteTheme.dimensions.spacingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingMedium),
    ) {
        CircularProgressIndicator(
            color = CimbyteTheme.colors.accent,
            trackColor = CimbyteTheme.colors.border,
        )
        Text(
            text = label,
            color = CimbyteTheme.colors.textMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
