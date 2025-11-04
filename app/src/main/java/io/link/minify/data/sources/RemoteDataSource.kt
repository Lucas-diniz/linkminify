package io.link.minify.data.sources

import io.link.minify.data.sources.model.CreateAliasRequest
import io.link.minify.data.sources.model.CreateAliasResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteDataSource {
    @POST("api/alias")
    suspend fun createAlias(@Body request: CreateAliasRequest): CreateAliasResponse
}

