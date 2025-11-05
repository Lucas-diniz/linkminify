package io.link.minify.ui

import io.link.minify.domain.error.LinkError
import io.link.minify.R
import io.link.minify.domain.error.NetworkError

fun LinkError.toMessageRes(): Int = when(this) {
    LinkError.Empty -> R.string.url_is_empty
    LinkError.InvalidScheme -> R.string.invalid_scheme
    LinkError.EmptyHost -> R.string.empty_host
    LinkError.HostContainsSpaces -> R.string.host_contains_spaces
    LinkError.Malformed -> R.string.malformed
    LinkError.AlreadyExists -> R.string.error_link_already_exists
}

fun NetworkError.toMessageRes(): Int = when(this) {
    NetworkError.NetworkUnavailable -> R.string.error_no_internet
    NetworkError.ServerError -> R.string.error_server
    NetworkError.Timeout -> R.string.error_timeout
    NetworkError.NotFound -> R.string.error_not_found
    NetworkError.Unknown -> R.string.error_unknown
    NetworkError.MissingFields -> R.string.missing_fields
}