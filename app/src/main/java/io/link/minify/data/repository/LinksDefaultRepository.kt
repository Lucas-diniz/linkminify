package io.link.minify.data.repository

import io.link.minify.data.sources.LocalDataSource
import io.link.minify.data.sources.RemoteDataSource
import io.link.minify.data.sources.model.CreateAliasRequest
import io.link.minify.data.toDomainError
import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.Link
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.repository.LinksRepository
import kotlinx.coroutines.flow.Flow

class LinksDefaultRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : LinksRepository {

    override fun listResentLinks(): Flow<List<MinifyLink>> {
        return localDataSource.getResentLinks()
    }

    override suspend fun shortenLink(url: String): NetWorkResult<MinifyLink> {
        return try {
            remoteDataSource.createAlias(CreateAliasRequest(url)).let {
                NetWorkResult.Success(
                    MinifyLink(
                        link = Link(
                            url = it.links.self
                        ),
                        alias = it.alias,
                        shortUrl = it.links.short,
                    )
                )
            }.also {
                localDataSource.setResentLink(it.data)
            }
        } catch (throwable: Throwable) {
            NetWorkResult.Error(throwable.toDomainError())
        }
    }
}