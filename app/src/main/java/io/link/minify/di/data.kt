package io.link.minify.di

import io.link.minify.data.repository.LinksDefaultRepository
import io.link.minify.data.sources.LocalDataSource
import io.link.minify.data.sources.RemoteDataSource
import io.link.minify.domain.repository.LinksRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataDi = module {

    single<LinksRepository> {
        LinksDefaultRepository(
            get<LocalDataSource>(),
            get<RemoteDataSource>()
        )
    }

    factory { LocalDataSource() }

    single { get<Retrofit>().create(RemoteDataSource::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl("https://url-shortener-server.onrender.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().also {
                        it.setLevel(HttpLoggingInterceptor.Level.BASIC)
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}