package io.link.minify.ui.mainScreen

import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.LinkError

data class MainScreenUiState(
    val listShortLinks: List<MinifyLink> = emptyList(),
    val isLoading: Boolean = true,
    val error: LinkError? = null,
    val errorMessage: Int? = null
)