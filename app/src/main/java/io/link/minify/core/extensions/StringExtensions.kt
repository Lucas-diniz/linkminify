package io.link.minify.core.extensions

import androidx.core.net.toUri
import io.link.minify.domain.error.LinkError

const val HTTP = "http"
const val HTTPS = "https"

/**
 * Validate the receiver string as an HTTP/HTTPS URL.
 *
 * The string is trimmed before validation. Returns a [Pair]<Boolean, LinkError?>
 * where the first element is `true` when the input is a valid URL and the second
 * is a nullable [LinkError] explaining why validation failed (or `null` when valid).
 *
 * Validation steps (in order):
 * 1. Empty input -> [LinkError.EMPTY]
 * 2. Parse to URI; on parse failure -> [LinkError.MALFORMED]
 * 3. Scheme must be "http" or "https" -> [LinkError.INVALID_SCHEME]
 * 4. Host must be present -> [LinkError.EMPTY_HOST]
 * 5. Host must not contain spaces -> [LinkError.HOST_CONTAINS_SPACES]
 * 6. Host must contain a dot that is not the first or last character -> [LinkError.MALFORMED]
 *
 * Examples:
 * - "https://example.com".isValidUrl() => (true, null)
 * - "ftp://example.com".isValidUrl() => (false, LinkError.INVALID_SCHEME)
 */
fun String.isValidUrl(): Pair<Boolean, LinkError?> {
    val input = this.trim()
    if (input.isEmpty()) return false to LinkError.EMPTY
    return try {
        val uri = input.toUri()
        val scheme = uri.scheme?.lowercase()
        val host = uri.host?.trim()

        if (scheme != HTTP && scheme != HTTPS) return false to LinkError.INVALID_SCHEME
        if (host.isNullOrEmpty()) return false to LinkError.EMPTY_HOST
        if (host.contains(" ")) return false to LinkError.HOST_CONTAINS_SPACES

        val dotIndex = host.indexOf('.')
        if (dotIndex <= 0 || dotIndex >= host.length - 1) {
            return false to LinkError.MALFORMED
        }

        true to null
    } catch (_: Exception) {
        false to LinkError.MALFORMED
    }
}