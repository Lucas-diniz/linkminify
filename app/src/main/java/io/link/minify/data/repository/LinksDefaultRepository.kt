package io.link.minify.data.repository

import io.link.minify.data.sources.LocalDataSource
import io.link.minify.data.sources.RemoteDataSource
import io.link.minify.data.sources.model.CreateAliasRequest
import io.link.minify.data.toDomainError
import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.LinkError
import io.link.minify.domain.error.NetworkError
import io.link.minify.domain.repository.LinksRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class LinksDefaultRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : LinksRepository {

    override fun listResentLinks(): Flow<List<MinifyLink>> {
        return localDataSource.getResentLinks()
    }

    override suspend fun shortenLink(url: String): NetWorkResult<MinifyLink> = try {
        if (localDataSource.verifyLinkExists(url)) {
            NetWorkResult.Error(LinkError.AlreadyExists)
        } else {
            val apiResponse = remoteDataSource.createAlias(CreateAliasRequest(url))
            val links = apiResponse.links
            val alias = apiResponse.alias

            if (links?.self == null || links.short == null || alias == null) {
                NetWorkResult.Error(NetworkError.MissingFields)
            } else {
                val minifyLinkResult = MinifyLink.create(
                    id = UUID.randomUUID().toString(),
                    url = links.self,
                    alias = alias,
                    shortUrl = links.short,
                    timestamp = System.currentTimeMillis()
                )

                minifyLinkResult.fold(
                    onSuccess = { minifyLink ->
                        localDataSource.setResentLink(minifyLink)
                        NetWorkResult.Success(minifyLink)
                    },
                    onFailure = { error ->
                        NetWorkResult.Error(NetworkError.MissingFields)
                    }
                )
            }
        }
    } catch (throwable: Throwable) {
        NetWorkResult.Error(throwable.toDomainError())
    }
}