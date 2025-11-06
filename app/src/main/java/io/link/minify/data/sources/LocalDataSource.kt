package io.link.minify.data.sources

import io.link.minify.domain.entity.MinifyLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LocalDataSource {
    private val _recentLinksStorage = MutableStateFlow<List<MinifyLink>>(emptyList())

    val recentLinks: Flow<List<MinifyLink>> get() = _recentLinksStorage

    fun saveMinifyLink(minifyLink: MinifyLink) {
        val currentList = _recentLinksStorage.value.toMutableList()
        currentList.add(0, minifyLink)
        _recentLinksStorage.value = currentList
    }

    fun verifyLinkExists(url: String): Boolean = _recentLinksStorage.value.any { it.hasSameUrlAs(url) }
}
