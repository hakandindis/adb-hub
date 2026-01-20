package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns

/**
 * Parser for extracting permissions from dumpsys output
 */
object PermissionsParser {
    /**
     * Extracts permissions from dumpsys output
     * Looks for both "requested permissions:" and "granted permissions:" sections
     */
    fun extractPermissions(output: String): List<String> {
        val permissions = mutableSetOf<String>()

        // Look for "requested permissions:" section
        val requestedPermissionsStart = output.indexOf(DumpsysParseStrings.REQUESTED_PERMISSIONS, ignoreCase = true)
        if (requestedPermissionsStart != -1) {
            // Find the end of permissions section (next major section starting with newline and spaces)
            val sectionEnd = output.indexOf("\n  ", requestedPermissionsStart + 1)
            val permissionsSection = if (sectionEnd != -1) {
                output.substring(requestedPermissionsStart, sectionEnd)
            } else {
                output.substring(requestedPermissionsStart)
            }

            // Extract permission names from lines like:
            // "android.permission.INTERNET"
            // "  android.permission.ACCESS_FINE_LOCATION"
            ParsePatterns.PERMISSION_LINE.findAll(permissionsSection).forEach { match ->
                permissions.add(match.groupValues[1])
            }
        }

        // Also look for "granted permissions:" section
        val grantedPermissionsStart = output.indexOf(DumpsysParseStrings.GRANTED_PERMISSIONS, ignoreCase = true)
        if (grantedPermissionsStart != -1) {
            val sectionEnd = output.indexOf("\n  ", grantedPermissionsStart + 1)
            val permissionsSection = if (sectionEnd != -1) {
                output.substring(grantedPermissionsStart, sectionEnd)
            } else {
                output.substring(grantedPermissionsStart)
            }

            ParsePatterns.PERMISSION_LINE.findAll(permissionsSection).forEach { match ->
                permissions.add(match.groupValues[1])
            }
        }

        // Fallback: search for common permission patterns throughout the output
        if (permissions.isEmpty()) {
            ParsePatterns.PERMISSION_FULL.findAll(output).forEach { match ->
                permissions.add(match.groupValues[1])
            }
        }

        return permissions.sorted().toList()
    }
}
