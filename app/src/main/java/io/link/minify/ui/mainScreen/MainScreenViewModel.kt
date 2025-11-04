package io.link.minify.ui.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.useCase.ListResentLinksUseCase
import io.link.minify.domain.useCase.ShortenLinkUseCase
import io.link.minify.ui.toMessageRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val listResentLinksUseCase: ListResentLinksUseCase,
    private val shortenLinkUseCase: ShortenLinkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadResentLinks()
    }

    private fun loadResentLinks() {
        viewModelScope.launch {
            listResentLinksUseCase().collect { links ->
                _uiState.update { it.copy(listShortLinks = links, isLoading = false) }
            }
        }
    }

    fun shortenLink(urlInput: String) {
        viewModelScope.launch {
            updateLoading(true)
            shortenLinkUseCase(urlInput).let { result ->
                when (result) {
                    is NetWorkResult.Error -> {
                        _uiState.update { it.copy(errorMessage = result.error.toMessageRes()) }
                    }

                    is NetWorkResult.Success<*> -> {}
                }
            }
            updateLoading(false)
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        _uiState.update { _uiState.value.copy(isLoading = isLoading) }
    }
}