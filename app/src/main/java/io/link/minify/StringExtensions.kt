package io.link.minify

import io.link.minify.domain.error.LinkError
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val HTTP = "http"
const val HTTPS = "https"

/**
 * Validate the receiver string as an HTTP/HTTPS URL.
 *
 * The string is trimmed before validation. Returns a [Pair]<Boolean, LinkError?> where the
 * first element is `true` when the input is a valid URL and the second is a nullable
 * [LinkError] explaining why validation failed (or `null` when valid).
 *
 * Validation steps (in order):
 * 1. Empty input -> [LinkError.Empty]
 * 2. Parse to URI; on parse failure -> [LinkError.Malformed]
 * 3. Scheme must be "http" or "https" -> [LinkError.InvalidScheme]
 * 4. Host must be present -> [LinkError.EmptyHost]
 * 5. Host must not contain spaces -> [LinkError.HostContainsSpaces]
 * 6. Host must contain a dot (`'.'`) that is not the first or last character -> [LinkError.Malformed]
 *
 * Examples:
 * - `https://example.com`.isValidUrl() -> (true, null)
 * - `ftp://example.com`.isValidUrl() -> (false, LinkError.InvalidScheme)
 */
fun String.isValidUrl(): Pair<Boolean, LinkError?> {
    val input = this.trim()
    if (input.isEmpty()) return false to LinkError.Empty
    return try {
        val uri = URI(input)
        val scheme = uri.scheme?.lowercase()
        val host = uri.host?.trim()

        if (scheme != HTTP && scheme != HTTPS) return false to LinkError.InvalidScheme
        if (host.isNullOrEmpty()) return false to LinkError.EmptyHost

        val dotIndex = host.indexOf('.')
        if (dotIndex <= 0 || dotIndex >= host.length - 1) {
            return false to LinkError.Malformed
        }

        true to null
    } catch (_: Exception) {
        false to LinkError.Malformed
    }
}

/**
 * Formats this Unix timestamp (milliseconds) into a human-readable string.
 *
 * Uses the device default locale and default time zone.
 *
 * @receiver Unix timestamp in milliseconds.
 * @return Formatted date string using pattern "MMM dd, yyyy 'at' HH:mm" (example: "Nov 03, 2025 at 14:30").
 *
 * Note: Uses SimpleDateFormat which is not thread-safe — avoid sharing instances across threads.
 */
fun Long.getFormatTimestamp(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}
