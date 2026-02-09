package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

import org.jetbrains.jewel.ui.icon.IconKey

data class ComponentDisplay(
    val name: String,
    val exported: Boolean,
    val icon: IconKey
) {
    val shortName: String get() = name.substringAfterLast("/", name)
}
