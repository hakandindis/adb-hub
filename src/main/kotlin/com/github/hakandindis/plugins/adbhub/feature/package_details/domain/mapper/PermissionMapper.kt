package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionSectionUiModel
import com.github.hakandindis.plugins.adbhub.models.PermissionSection

/**
 * Mapper for converting PermissionSection to UI models
 */
object PermissionMapper {

    fun toUiModel(section: PermissionSection): PermissionSectionUiModel {
        return PermissionSectionUiModel(
            sectionType = section.sectionType,
            sectionTitle = section.sectionTitle,
            items = section.items.map {
                PermissionItemUiModel(
                    name = it.name,
                    detail = it.detail,
                    permissionType = section.sectionType
                )
            }
        )
    }

    fun toUiModels(sections: List<PermissionSection>): List<PermissionSectionUiModel> {
        return sections.map { toUiModel(it) }
    }
}
