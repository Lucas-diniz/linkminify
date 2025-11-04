package io.link.minify.di

import io.link.minify.domain.useCase.ListResentLinksUseCase
import io.link.minify.domain.useCase.ShortenLinkUseCase
import io.link.minify.ui.mainScreen.MainScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiDi = module {
    viewModel {
        MainScreenViewModel(
            get<ListResentLinksUseCase>(),
            get<ShortenLinkUseCase>()
        )
    }
}