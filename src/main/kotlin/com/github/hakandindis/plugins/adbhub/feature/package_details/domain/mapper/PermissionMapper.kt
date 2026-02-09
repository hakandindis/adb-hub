package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionItemDisplay
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionSectionUiModel
import com.github.hakandindis.plugins.adbhub.models.PermissionSection
import com.github.hakandindis.plugins.adbhub.models.PermissionSectionType

object PermissionMapper {

    fun toUiModel(section: PermissionSection): PermissionSectionUiModel {
        return PermissionSectionUiModel(
            sectionType = section.sectionType,
            sectionTitle = section.sectionTitle,
            items = section.items.map {
                PermissionItemUiModel(
                    name = it.name,
                    permissionDisplay = toDisplay(it.detail, section.sectionType),
                    permissionType = section.sectionType
                )
            }
        )
    }

    fun toUiModels(sections: List<PermissionSection>): List<PermissionSectionUiModel> {
        return sections.map { toUiModel(it) }
    }

    private fun toDisplay(detail: String?, sectionType: PermissionSectionType): PermissionItemDisplay {
        return when {
            detail == "granted=true" && sectionType.showsGrantedBadge() -> PermissionItemDisplay.GrantedBadge
            detail == "granted=false" && sectionType.showsGrantedBadge() -> PermissionItemDisplay.DeniedBadge
            else -> PermissionItemDisplay.DetailText(detail ?: "")
        }
    }
}
