package io.link.minify.domain.entity

/**
 * Represents a shortened URL link with its metadata.
 *
 * This is a rich domain entity that encapsulates business rules and behaviors
 *
 * @property id Unique identifier for this link - ID must not be blank
 * @property url Original long URL that was shortened - URL must not be blank
 * @property alias Short alias/code for the link - Alias must not be blank
 * @property shortUrl Complete shortened URL (base + alias) - Short URL must not be blank
 * @property timestamp Unix timestamp in milliseconds when link was created
 */
@ConsistentCopyVisibility
data class MinifyLink private constructor(
    val id: String,
    val url: String,
    val alias: String,
    val shortUrl: String,
    val timestamp: Long,
) {
    companion object {
        private const val ID_CANNOT_BE_BLANK = "ID cannot be blank"
        private const val URL_CANNOT_BE_BLANK = "URL cannot be blank"
        private const val ALIAS_CANNOT_BE_BLANK = "Alias cannot be blank"
        private const val SHORT_URL_CANNOT_BE_BLANK = "Short URL cannot be blank"
        private const val TIMESTAMP_MUST_BE_POSITIVE = "Timestamp must be positive"

        fun create(
            id: String,
            url: String,
            alias: String,
            shortUrl: String,
            timestamp: Long,
        ): Result<MinifyLink> =
            runCatching {
                require(id.isNotBlank()) { ID_CANNOT_BE_BLANK }
                require(url.isNotBlank()) { URL_CANNOT_BE_BLANK }
                require(alias.isNotBlank()) { ALIAS_CANNOT_BE_BLANK }
                require(shortUrl.isNotBlank()) { SHORT_URL_CANNOT_BE_BLANK }
                require(timestamp > 0) { TIMESTAMP_MUST_BE_POSITIVE }

                MinifyLink(
                    id = id,
                    url = url,
                    alias = alias,
                    shortUrl = shortUrl,
                    timestamp = timestamp,
                )
            }
    }

    fun hasSameUrlAs(otherUrl: String): Boolean = url.trim().equals(otherUrl.trim(), ignoreCase = true)
}
