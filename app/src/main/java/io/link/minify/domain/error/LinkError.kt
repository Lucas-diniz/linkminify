package io.link.minify.domain.error


sealed class LinkError {
    data object Empty : LinkError()
    data object InvalidScheme : LinkError()
    data object EmptyHost : LinkError()
    data object HostContainsSpaces : LinkError()
    data object Malformed : LinkError()
}