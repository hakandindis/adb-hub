package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

import org.jetbrains.jewel.ui.icon.IconKey

/**
 * Shared UI model for component list rows (Activities, Receivers, Services, Content Providers).
 * Used by ComponentListTab to render a unified list with tab-specific icon.
 */
data class ComponentDisplay(
    val name: String,
    val exported: Boolean,
    val icon: IconKey
) {
    val shortName: String get() = name.substringAfterLast("/", name)
}
