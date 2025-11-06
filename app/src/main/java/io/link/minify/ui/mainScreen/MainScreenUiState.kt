package io.link.minify.ui.mainScreen

import io.link.minify.domain.entity.MinifyLink

data class MainScreenUiState(
    val listShortLinks: List<MinifyLink> = emptyList(),
    val isLoading: Boolean = true,
    val message: Int? = null,
)
