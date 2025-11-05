package io.link.minify.ui.mainScreen

import io.link.minify.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.LinkError
import io.link.minify.domain.error.NetworkError
import io.link.minify.domain.useCase.ListResentLinksUseCase
import io.link.minify.domain.useCase.ShortenLinkUseCase
import io.link.minify.ui.toMessageRes
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

    fun shortenLink(urlInput: String) {
        viewModelScope.launch {
            runCatching {
                updateLoading(true)
                shortenLinkResultHandler(shortenLinkUseCase(urlInput))
            }.onFailure {
                _uiState.update { it.copy(message = R.string.shortenLink_error) }
            }.also {
                updateLoading(false)
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(message = null) }
    }

    private fun loadResentLinks() {
        viewModelScope.launch {
            runCatching {
                listResentLinksUseCase().collect { links ->
                    _uiState.update { it.copy(listShortLinks = links, isLoading = false) }
                }
            }.onFailure {
                _uiState.update { it.copy(message = R.string.list_error) }
            }.also {
                updateLoading(false)
            }
        }
    }

    private fun shortenLinkResultHandler(result: NetWorkResult<MinifyLink>) {
        when (result) {
            is NetWorkResult.Success<*> -> {
                sendMessage(R.string.success_message)
            }

            is NetWorkResult.Error -> {
                when (result.error) {
                    is LinkError -> sendMessage(result.error.toMessageRes())
                    is NetworkError -> sendMessage(result.error.toMessageRes())
                }
            }
        }
    }

    private fun sendMessage(messageId: Int) {
        _uiState.update { it.copy(message = messageId) }
    }

    private fun updateLoading(isLoading: Boolean) {
        _uiState.update { _uiState.value.copy(isLoading = isLoading) }
    }
}