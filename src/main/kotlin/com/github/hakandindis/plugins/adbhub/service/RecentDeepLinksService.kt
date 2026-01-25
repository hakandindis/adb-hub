package com.github.hakandindis.plugins.adbhub.service

import com.intellij.openapi.components.*

/**
 * Application-level service that persists recently executed deep link URIs.
 * Used to show "Recent Deep Links" and auto-fill the URI field on click.
 */
@State(
    name = "AdbHubRecentDeepLinks",
    storages = [
        Storage(value = "adbhub-recent-deeplinks.xml", roamingType = RoamingType.DISABLED)
    ]
)
@Service(Service.Level.APP)
class RecentDeepLinksService :
    SimplePersistentStateComponent<RecentDeepLinksService.RecentDeepLinksState>(RecentDeepLinksState()) {

    class RecentDeepLinksState : BaseState() {
        var recentUris: MutableList<String> = ArrayList()

        fun replaceUrisAndMarkModified(newList: List<String>) {
            recentUris.clear()
            recentUris.addAll(newList)
            incrementModificationCount()
        }
    }

    private val state: RecentDeepLinksState
        get() = getState()

    /**
     * Adds the URI to the front of the list, removes any prior occurrence,
     * and keeps at most [maxSize] items. Persists to XML.
     */
    fun addAndTruncate(uri: String, maxSize: Int = 20) {
        val trimmed = uri.trim()
        if (trimmed.isBlank()) return
        val current = state.recentUris
        val newList = listOf(trimmed) + current.filter { it != trimmed }.take(maxSize - 1)
        state.replaceUrisAndMarkModified(newList)
    }

    /**
     * Returns the list of recent URIs (newest first). Read-only copy.
     */
    fun getRecentUris(): List<String> = state.recentUris.toList()
}
