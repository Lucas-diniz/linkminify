package io.link.minify

import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.NetworkError
import io.link.minify.domain.repository.LinksRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

@Suppress("ktlint:standard:function-naming")
class LinksFakeRepository : LinksRepository {
    private val _links = MutableStateFlow<List<MinifyLink>>(emptyList())

    var shouldFailOnCreate = false
    var shouldReturnAlreadyExists = false
    var createShortLinkDelayMs = 0L

    fun setLinks(links: List<MinifyLink>) {
        _links.value = links
    }

    fun addLink(link: MinifyLink) {
        _links.value = _links.value + link
    }

    fun clearLinks() {
        _links.value = emptyList()
    }

    override fun getResentLinks(): Flow<List<MinifyLink>> = _links

    override fun verifyIfUrlExists(url: String): Boolean =
        shouldReturnAlreadyExists ||
            _links.value.any {
                it.hasSameUrlAs(url)
            }

    override fun saveMinifyLink(minifyLink: MinifyLink) {
        _links.value = listOf(minifyLink) + _links.value
    }

    override suspend fun createShortLink(url: String): NetWorkResult<MinifyLink> {
        if (createShortLinkDelayMs > 0) {
            delay(createShortLinkDelayMs)
        }

        if (shouldFailOnCreate) {
            return NetWorkResult.Error(NetworkError.Unknown)
        }

        val link =
            MinifyLink
                .create(
                    id = UUID.randomUUID().toString(),
                    url = url,
                    alias = "alias${UUID.randomUUID().toString().take(6)}",
                    shortUrl = "https://lnk.mn/${UUID.randomUUID().toString().take(8)}",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        return NetWorkResult.Success(link)
    }
}
