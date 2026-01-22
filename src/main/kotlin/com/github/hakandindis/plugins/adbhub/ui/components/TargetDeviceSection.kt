package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text

@Composable
fun TargetDeviceSection(
    devices: List<Device>,
    selectedDevice: Device?,
    deviceInfo: DeviceInfo?,
    onDeviceSelected: (Device) -> Unit,
    onRefreshDevices: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            "TARGET DEVICE",
            style = JewelTheme.defaultTextStyle
        )
        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(AdbHubShapes.SM)
                            .background(AdbHubTheme.itemHover)
                            .border(1.dp, AdbHubTheme.border, AdbHubShapes.SM)
                            .clickable { if (devices.isNotEmpty()) expanded = !expanded }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            selectedDevice?.displayName ?: "Select device",
                            style = JewelTheme.defaultTextStyle
                        )
                        Icon(
                            AdbIcons.arrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (expanded && devices.isNotEmpty()) {
                        Popup(
                            alignment = Alignment.BottomStart,
                            offset = IntOffset(0, 4),
                            onDismissRequest = { expanded = false }
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(min = 220.dp)
                                    .clip(AdbHubShapes.SM)
                                    .background(AdbHubTheme.surface)
                                    .border(1.dp, AdbHubTheme.border, AdbHubShapes.SM)
                                    .padding(4.dp)
                            ) {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    items(devices) { device ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(AdbHubShapes.XS)
                                                .clickable {
                                                    onDeviceSelected(device)
                                                    expanded = false
                                                }
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                AdbIcons.smartphone,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                                Text(device.displayName)
                                                Text(
                                                    device.id,
                                                    style = JewelTheme.defaultTextStyle
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AdbHubTheme.itemHover)
                        .border(1.dp, AdbHubTheme.border, CircleShape)
                        .clickable(
                            onClick = {
                                onRefreshDevices?.invoke()
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        AdbIcons.refresh,
                        contentDescription = "Refresh devices",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        if (selectedDevice != null && deviceInfo != null) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AdbHubShapes.SM)
                    .background(AdbHubTheme.background)
                    .border(1.dp, AdbHubTheme.border.copy(alpha = 0.5f), AdbHubShapes.SM)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DeviceDetailRow("Manufacturer", deviceInfo.manufacturer ?: "—")
                    DeviceDetailRow("Density", deviceInfo.screenDensity ?: "—")
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DeviceDetailRow("Android Ver", deviceInfo.androidInfo.takeIf { it.isNotBlank() } ?: "—")
                    DeviceDetailRow("CPU/ABI", deviceInfo.cpuAbi ?: "—")
                }
            }
        }
    }
}

@Composable
private fun DeviceDetailRow(label: String, value: String) {
    Column {
        Text(
            label,
            style = JewelTheme.defaultTextStyle
        )
        Text(
            value,
            style = JewelTheme.defaultTextStyle
        )
    }
}
