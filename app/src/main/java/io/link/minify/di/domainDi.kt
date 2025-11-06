package io.link.minify.di

import io.link.minify.domain.repository.LinksRepository
import io.link.minify.domain.useCase.ListResentLinksUseCase
import io.link.minify.domain.useCase.ShortenLinkUseCase
import org.koin.dsl.module

val domainDi =
    module {
        factory { ListResentLinksUseCase(get<LinksRepository>()) }
        factory { ShortenLinkUseCase(get<LinksRepository>()) }
    }
