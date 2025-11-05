package io.link.minify.domain.repository

import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import kotlinx.coroutines.flow.Flow

interface LinksRepository {
    fun getResentLinks(): Flow<List<MinifyLink>>
    fun verifyIfUrlExists(url: String): Boolean
    fun saveMinifyLink(minifyLink: MinifyLink)
    suspend fun createShortLink(url: String): NetWorkResult<MinifyLink>
}