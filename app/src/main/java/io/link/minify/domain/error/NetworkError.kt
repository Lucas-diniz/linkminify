package io.link.minify.domain.error

sealed class NetworkError {
    data object NetworkUnavailable : NetworkError()
    data object ServerError : NetworkError()
    data object Timeout : NetworkError()
    data object NotFound : NetworkError()
    data object Unknown : NetworkError()
}