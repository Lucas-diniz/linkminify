package io.link.minify.domain.useCase

import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.LinkError
import io.link.minify.domain.repository.LinksRepository
import io.link.minify.isValidUrl

class ShortenLinkUseCase(private val repository: LinksRepository) {
    suspend operator fun invoke(url: String): NetWorkResult<MinifyLink> {

        url.isValidUrl().second?.let { validationError ->
            return NetWorkResult.Error(validationError)
        }

        if (repository.verifyIfUrlExists(url)) {
            return NetWorkResult.Error(LinkError.AlreadyExists)
        }

        return repository.createShortLink(url).onSuccess { minifyLink ->
            repository.saveMinifyLink(minifyLink)
        }
    }
}