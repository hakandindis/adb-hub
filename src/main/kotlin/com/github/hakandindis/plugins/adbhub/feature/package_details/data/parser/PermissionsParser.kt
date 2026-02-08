package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser.PermissionsParser.parsePermissionSections
import com.github.hakandindis.plugins.adbhub.models.PermissionItem
import com.github.hakandindis.plugins.adbhub.models.PermissionSection
import com.github.hakandindis.plugins.adbhub.models.PermissionSectionType

/**
 * Parser for extracting permission sections from dumpsys package output.
 * Parses: declared permissions, requested permissions, install permissions, runtime permissions.
 */
object PermissionsParser {

    private const val SECTION_INDENT = "      " // 6 spaces in dumpsys output

    /**
     * Parses all permission sections from dumpsys package output.
     * Returns sections in order: Declared, Requested, Install, Runtime (when present).
     */
    fun parsePermissionSections(output: String): List<PermissionSection> {
        val sections = mutableListOf<PermissionSection>()

        parseSection(
            output,
            DumpsysParseStrings.DECLARED_PERMISSIONS,
            PermissionSectionType.DECLARED,
            "Declared Permissions"
        )?.let {
            sections.add(it)
        }
        parseSection(
            output,
            DumpsysParseStrings.REQUESTED_PERMISSIONS,
            PermissionSectionType.REQUESTED,
            "Requested Permissions"
        )?.let {
            sections.add(it)
        }
        parseSection(
            output,
            DumpsysParseStrings.INSTALL_PERMISSIONS,
            PermissionSectionType.INSTALL,
            "Install Permissions"
        )?.let {
            sections.add(it)
        }
        parseSection(
            output,
            DumpsysParseStrings.RUNTIME_PERMISSIONS,
            PermissionSectionType.RUNTIME,
            "Runtime Permissions"
        )?.let {
            sections.add(it)
        }

        return sections
    }

    private fun parseSection(
        output: String,
        header: String,
        sectionType: PermissionSectionType,
        sectionTitle: String
    ): PermissionSection? {
        val startIndex = output.indexOf(header, ignoreCase = true)
        if (startIndex == -1) return null

        val contentStart = startIndex + header.length
        val linesAfterHeader = output.substring(contentStart).lineSequence().toList()

        val items = mutableListOf<PermissionItem>()
        for (line in linesAfterHeader) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            if (sectionType == PermissionSectionType.RUNTIME) {
                val lower = trimmed.lowercase()
                if (lower.startsWith(DumpsysParseStrings.ENABLED_COMPONENTS_HEADER.lowercase()) ||
                    lower.startsWith(DumpsysParseStrings.DISABLED_COMPONENTS_HEADER.lowercase())
                ) {
                    break
                }
            }

            if (!line.startsWith(SECTION_INDENT) && line.isNotBlank()) break

            val colonIndex = trimmed.indexOf(':')
            var (name, detail) = if (colonIndex >= 0) {
                trimmed.substring(0, colonIndex).trim() to
                        trimmed.substring(colonIndex + 1).trim().takeIf { it.isNotEmpty() }
            } else {
                trimmed to null
            }

            when (sectionType) {
                PermissionSectionType.DECLARED -> if (detail != null && detail.startsWith("prot=")) detail = null
                PermissionSectionType.RUNTIME -> detail = detail?.let { normalizeRuntimePermissionDetail(it) }
                else -> {}
            }

            if (name.isNotEmpty()) {
                items.add(PermissionItem(name = name, detail = detail))
            }
        }

        return if (items.isEmpty()) null else PermissionSection(
            sectionType = sectionType,
            sectionTitle = sectionTitle,
            items = items
        )
    }

    /** Keeps only granted=true or granted=false; strips flags and other parts */
    private fun normalizeRuntimePermissionDetail(raw: String): String? {
        return when {
            raw.contains("granted=true") -> "granted=true"
            raw.contains("granted=false") -> "granted=false"
            else -> raw.takeIf { it.isNotEmpty() }
        }
    }

    /**
     * Extracts a flat list of permission names from "requested permissions" section (legacy).
     * Prefer [parsePermissionSections] for full structured data.
     */
    fun extractPermissions(output: String): List<String> {
        val sections = parsePermissionSections(output)
        return sections.flatMap { it.items.map { item -> item.name } }.distinct().sorted()
    }
}
