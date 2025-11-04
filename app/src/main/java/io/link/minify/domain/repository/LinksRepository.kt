package io.link.minify.domain.repository

import io.link.minify.domain.entity.MinifyLink
import kotlinx.coroutines.flow.Flow

interface LinksRepository {
    fun listResentLinks(): Flow<List<MinifyLink>>
    suspend fun shortenLink(url: String): Result<MinifyLink>
}