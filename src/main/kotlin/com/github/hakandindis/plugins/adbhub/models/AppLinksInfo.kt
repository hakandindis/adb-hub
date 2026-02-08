package com.github.hakandindis.plugins.adbhub.models

/**
 * App Links / domain verification info from `adb shell pm get-app-links`.
 *
 * @see [Verify App Links](https://developer.android.com/training/app-links/verify-applinks)
 */
data class AppLinksInfo(
    val packageName: String,
    val id: String?,
    val signatures: String?,
    val domainVerificationStates: List<DomainVerificationState>
) {
    data class DomainVerificationState(
        val domain: String,
        /** State: "verified", "none", "approved", "denied", "legacy_failure", etc., or numeric code (e.g. "1024") */
        val state: String
    )
}
