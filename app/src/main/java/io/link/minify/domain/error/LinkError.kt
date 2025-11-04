package io.link.minify.domain.error


enum class LinkError(val message: String) {
    EMPTY("Empty"),
    INVALID_SCHEME("InvalidScheme"),
    EMPTY_HOST("EmptyHost"),
    HOST_CONTAINS_SPACES("HostContainsSpaces"),
    MALFORMED("Malformed")
}