package io.link.minify.domain.useCase

import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.repository.LinksRepository

class ShortenLinkUseCase(private val repository: LinksRepository) {
    suspend operator fun invoke(url: String): NetWorkResult<MinifyLink> {
        return repository.shortenLink(url)
    }
}