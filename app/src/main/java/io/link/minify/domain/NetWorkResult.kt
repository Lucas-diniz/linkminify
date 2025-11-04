package io.link.minify.domain

import io.link.minify.domain.error.NetworkError

sealed class NetWorkResult<out T> {
    data class Success<T>(val data: T) : NetWorkResult<T>()
    data class Error(val error: NetworkError) : NetWorkResult<Nothing>()
}