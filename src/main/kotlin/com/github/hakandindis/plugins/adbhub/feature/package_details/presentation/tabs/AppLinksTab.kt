package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.components.PathCard
import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

private val SUCCESS_STATES = setOf(
    "verified",
    "approved",
    "restored",
    "system_configured",
    "migrated"
)

@Composable
fun AppLinksTab(
    appLinks: AppLinksInfo?,
    onCopy: (String) -> Unit = {}
) {
    if (appLinks == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No App Links data available",
                style = JewelTheme.defaultTextStyle,
                color = AdbHubTheme.textMuted
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PathCard(
            label = "APP ID",
            path = appLinks.packageName,
            onCopy = onCopy
        )

        PathCard(
            label = "SIGNATURES (SHA-256)",
            path = appLinks.signatures ?: "—",
            onCopy = onCopy
        )

        if (appLinks.domainVerificationStates.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AdbHubShapes.SM)
                    .background(AdbHubTheme.surface.copy(alpha = 0.3f))
                    .border(1.dp, AdbHubTheme.border, AdbHubShapes.SM)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AdbHubTheme.surface.copy(alpha = 0.5f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "DOMAIN",
                            style = JewelTheme.defaultTextStyle
                        )
                        Text(
                            "STATE",
                            style = JewelTheme.defaultTextStyle
                        )
                    }
                    LazyColumn {
                        items(appLinks.domainVerificationStates) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    item.domain,
                                    style = JewelTheme.defaultTextStyle
                                )
                                Text(
                                    item.state,
                                    style = JewelTheme.defaultTextStyle,
                                    color = domainStateColor(item.state)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun domainStateColor(state: String): Color {
    val normalized = state.lowercase().trim()
    return when {
        normalized in SUCCESS_STATES -> AdbHubTheme.success
        normalized == "none" || normalized == "denied" || normalized == "legacy_failure" ->
            AdbHubTheme.warning

        state.toIntOrNull() != null && state.toIntOrNull()!! >= 1024 ->
            AdbHubTheme.warning

        else -> AdbHubTheme.textMain
    }
}
