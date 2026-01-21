package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionUiModel
import com.github.hakandindis.plugins.adbhub.models.PermissionStatus

/**
 * Mapper for converting PermissionStatus to UI models
 */
object PermissionMapper {
    /**
     * Converts PermissionStatus to PermissionUiModel
     */
    fun toUiModel(permission: PermissionStatus): PermissionUiModel {
        return PermissionUiModel(
            name = permission.permission,
            status = permission
        )
    }

    /**
     * Converts list of PermissionStatus to list of PermissionUiModel
     */
    fun toUiModels(permissions: List<PermissionStatus>): List<PermissionUiModel> {
        return permissions.map { toUiModel(it) }
    }
}
