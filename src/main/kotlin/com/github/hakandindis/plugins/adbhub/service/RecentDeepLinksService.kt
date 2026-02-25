package com.github.hakandindis.plugins.adbhub.service

import com.intellij.openapi.components.*

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

    fun addAndTruncate(uri: String, maxSize: Int = 20) {
        val trimmed = uri.trim()
        if (trimmed.isBlank()) return
        val current = state.recentUris
        val newList = listOf(trimmed) + current.filter { it != trimmed }.take(maxSize - 1)
        state.replaceUrisAndMarkModified(newList)
    }

    fun getRecentUris(): List<String> = state.recentUris.toList()

    fun remove(uri: String) {
        val newList = state.recentUris.filter { it != uri }
        state.replaceUrisAndMarkModified(newList)
    }
}
