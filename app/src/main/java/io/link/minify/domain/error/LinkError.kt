package io.link.minify.domain.error


sealed class LinkError: MinifyError {
    data object Empty : LinkError()
    data object InvalidScheme : LinkError()
    data object EmptyHost : LinkError()
    data object HostContainsSpaces : LinkError()
    data object Malformed : LinkError()
    data object AlreadyExists : LinkError()
}