package ro.cimbyte.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Cimbyte catalog — dark",
    showBackground = true,
    backgroundColor = 0xFF0E1218,
    widthDp = 420,
    heightDp = 900,
)
@Composable
private fun CimbyteCatalogDarkPreview() {
    CimbyteTheme(darkTheme = true) {
        CimbyteCatalog()
    }
}

@Preview(
    name = "Cimbyte catalog — light",
    showBackground = true,
    backgroundColor = 0xFFF7F8FA,
    widthDp = 420,
    heightDp = 900,
)
@Composable
private fun CimbyteCatalogLightPreview() {
    CimbyteTheme(darkTheme = false) {
        CimbyteCatalog()
    }
}

@Composable
private fun CimbyteCatalog() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CimbyteTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(CimbyteTheme.dimensions.spacingMedium),
        verticalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingLarge),
    ) {
        CimbyteSectionHeader(
            title = "Cimbyte UI",
            subtitle = "Compose component catalog",
            trailingContent = {
                CimbyteBadge("Android", variant = CimbyteBadgeVariant.Accent)
            },
        )

        CimbyteCard(modifier = Modifier.fillMaxWidth()) {
            Text("Card", style = CimbyteTheme.typography.titleMedium)
            Text(
                "A neutral container for application content.",
                color = CimbyteTheme.colors.textSecondary,
                style = CimbyteTheme.typography.bodyMedium,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall)) {
            Row(horizontalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall)) {
                CimbyteBadge("Neutral")
                CimbyteBadge("Accent", variant = CimbyteBadgeVariant.Accent)
                CimbyteBadge("Success", variant = CimbyteBadgeVariant.Success)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall)) {
                CimbyteBadge("Warning", variant = CimbyteBadgeVariant.Warning)
                CimbyteBadge("Error", variant = CimbyteBadgeVariant.Error)
                CimbyteBadge("Info", variant = CimbyteBadgeVariant.Info)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall)) {
            Row(horizontalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall)) {
                CimbyteButton(onClick = {}) { Text("Primary") }
                CimbyteButton(onClick = {}, variant = CimbyteButtonVariant.Accent) { Text("Accent") }
                CimbyteButton(onClick = {}, variant = CimbyteButtonVariant.Secondary) { Text("Secondary") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(CimbyteTheme.dimensions.spacingSmall)) {
                CimbyteButton(onClick = {}, variant = CimbyteButtonVariant.Ghost) { Text("Ghost") }
                CimbyteButton(onClick = {}, variant = CimbyteButtonVariant.Danger) { Text("Danger") }
                CimbyteButton(onClick = {}, enabled = false) { Text("Disabled") }
            }
        }

        CimbyteEmptyState(
            title = "Nothing here yet",
            description = "Create the first item to get started.",
            icon = { Text("＋", style = CimbyteTheme.typography.headlineSmall) },
            action = {
                CimbyteButton(onClick = {}, variant = CimbyteButtonVariant.Accent) {
                    Text("Create item")
                }
            },
        )

        CimbyteCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(),
        ) {
            CimbyteSettingsRow(
                title = "Automatic updates",
                description = "Install stable releases when available.",
                trailingContent = {
                    CimbyteBadge("On", variant = CimbyteBadgeVariant.Success)
                },
            )
        }

        CimbyteCodeBlock(
            code = "val result = items.filter { it.active }",
            label = "example.kt",
            modifier = Modifier.fillMaxWidth(),
        )

        CimbyteLoadingState(
            modifier = Modifier.fillMaxWidth(),
            label = "Loading workspace",
        )
    }
}
