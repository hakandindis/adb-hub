package com.github.hakandindis.plugins.adbhub.models

data class AppLinksInfo(
    val packageName: String,
    val id: String?,
    val signatures: String?,
    val domainVerificationStates: List<DomainVerificationState>
) {
    data class DomainVerificationState(
        val domain: String,
        val state: String
    )
}
