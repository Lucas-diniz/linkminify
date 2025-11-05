package io.link.minify.domain.useCase

import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.repository.LinksRepository
import io.link.minify.isValidUrl

class ShortenLinkUseCase(private val repository: LinksRepository) {
    suspend operator fun invoke(url: String): NetWorkResult<MinifyLink> {
        url.isValidUrl().second?.let { NetWorkResult.Error(it) }
        return repository.shortenLink(url)
    }
}