package io.link.minify.domain

import io.link.minify.domain.error.MinifyError

sealed class NetWorkResult<out T> {
    data class Success<T>(
        val data: T,
    ) : NetWorkResult<T>()

    data class Error(
        val error: MinifyError,
    ) : NetWorkResult<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): NetWorkResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (MinifyError) -> Unit): NetWorkResult<T> {
        if (this is Error) action(error)
        return this
    }
}
