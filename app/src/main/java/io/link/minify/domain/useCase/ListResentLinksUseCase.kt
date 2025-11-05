package io.link.minify.domain.useCase

import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.repository.LinksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListResentLinksUseCase(private val repository: LinksRepository) {
    operator fun invoke(): Flow<List<MinifyLink>> {
        return repository.getResentLinks().map { minifyLink ->
            minifyLink
                .take(20)
        }
    }
}