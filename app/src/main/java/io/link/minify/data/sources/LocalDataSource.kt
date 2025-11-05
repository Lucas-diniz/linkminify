package io.link.minify.data.sources

import io.link.minify.domain.entity.MinifyLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalDataSource {
    private val _recentLinksStorage = MutableStateFlow<List<MinifyLink>>(emptyList())
    private val recentLinksStorage: StateFlow<List<MinifyLink>> = _recentLinksStorage.asStateFlow()

    fun getResentLinks(): Flow<List<MinifyLink>> {
        return recentLinksStorage
    }

    fun setResentLink(minifyLink: MinifyLink) {
        val currentList = _recentLinksStorage.value.toMutableList()
        currentList.add(0, minifyLink)
        _recentLinksStorage.value = currentList
    }

    fun verifyLinkExists(url: String): Boolean {
        return _recentLinksStorage.value.any { it.url == url }
    }
}