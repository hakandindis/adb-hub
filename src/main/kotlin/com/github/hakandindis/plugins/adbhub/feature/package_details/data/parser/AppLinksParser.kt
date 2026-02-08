package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo

/**
 * Parses output of `adb shell pm get-app-links PACKAGE_NAME`.
 *
 * Example output:
 *   com.Slack:
 *     ID: 131b8c28-48f0-46de-9769-d8e374fb0f54
 *     Signatures: [33:53:36:19:75:6E:...]
 *     Domain verification state:
 *       join.slack-gov.com: verified
 *       app.slack-gov.com: verified
 */
object AppLinksParser {

    private const val ID_PREFIX = "ID:"
    private const val SIGNATURES_PREFIX = "Signatures:"
    private const val DOMAIN_VERIFICATION_HEADER = "Domain verification state:"

    fun parse(output: String, packageName: String): AppLinksInfo {
        val lines = output.lines()
        var id: String? = null
        var signatures: String? = null
        val domainStates = mutableListOf<AppLinksInfo.DomainVerificationState>()
        var inDomainSection = false

        for (line in lines) {
            val trimmed = line.trim()
            when {
                trimmed.startsWith(ID_PREFIX) ->
                    id = trimmed.substringAfter(ID_PREFIX).trim()

                trimmed.startsWith(SIGNATURES_PREFIX) -> {
                    val value = trimmed.substringAfter(SIGNATURES_PREFIX).trim()
                    signatures = value.removeSurrounding("[", "]").takeIf { it.isNotEmpty() }
                }

                trimmed.equals(DOMAIN_VERIFICATION_HEADER, ignoreCase = true) ->
                    inDomainSection = true

                inDomainSection && trimmed.contains(":") -> {
                    val colonIndex = trimmed.indexOf(':')
                    val domain = trimmed.substring(0, colonIndex).trim()
                    val state = trimmed.substring(colonIndex + 1).trim()
                    if (domain.isNotEmpty() && state.isNotEmpty()) {
                        domainStates.add(AppLinksInfo.DomainVerificationState(domain = domain, state = state))
                    }
                }
            }
        }

        return AppLinksInfo(
            packageName = packageName,
            id = id?.takeIf { it.isNotEmpty() },
            signatures = signatures?.takeIf { it.isNotEmpty() },
            domainVerificationStates = domainStates
        )
    }
}
