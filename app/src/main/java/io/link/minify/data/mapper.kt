package io.link.minify.data

import io.link.minify.domain.error.NetworkError
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toDomainError(): NetworkError = when(this) {
    is UnknownHostException -> NetworkError.NetworkUnavailable
    is SocketTimeoutException -> NetworkError.Timeout
    is HttpException -> when(code()) {
        404 -> NetworkError.NotFound
        in 500..599 -> NetworkError.ServerError
        else -> NetworkError.Unknown
    }
    else -> NetworkError.Unknown
}