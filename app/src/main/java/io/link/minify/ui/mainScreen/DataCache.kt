package io.link.minify.ui.mainScreen

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DataCache
 *
 * Central repository for data models, helper functions, and sample data used throughout the application.
 * This object follows the Single Responsibility Principle by managing only data-related concerns.
 *
 * SOLID Principles Applied:
 * - Single Responsibility: Handles only data models and data-related utilities
 * - Open/Closed: Can be extended with new data types or helpers without modifying existing code
 * - Dependency Inversion: Provides abstractions (data models) that other components depend on
 */
object DataCache {

    /**
     * Data class representing a shortened link with all relevant information.
     *
     * This is an immutable data class that encapsulates all properties of a shortened link.
     * It follows the Single Responsibility Principle by representing only link data.
     *
     * @property id Unique identifier for the link
     * @property originalUrl The original long URL that was shortened
     * @property shortUrl The generated short URL
     * @property alias The alias/code used in the short URL
     * @property timestamp Unix timestamp (in milliseconds) when the link was created
     * @property clickCount Number of times the shortened link has been clicked
     */
    data class ShortenedLink(
        val id: String,
        val originalUrl: String,
        val shortUrl: String,
        val alias: String,
        val timestamp: Long,
        val clickCount: Int = 0
    )

    /**
     * Formats a Unix timestamp into a human-readable string.
     *
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted date string in the format "MMM dd, yyyy 'at' HH:mm" (e.g., "Nov 03, 2025 at 14:30")
     */
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Generates a random alias for a shortened URL.
     *
     * Creates a 6-character random string composed of alphanumeric characters (a-z, A-Z, 0-9).
     * This provides 62^6 = ~56.8 billion possible combinations.
     *
     * @return A random 6-character alphanumeric string
     */
    fun generateAlias(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }

    /**
     * Provides sample shortened links for testing and preview purposes.
     *
     * Generates a list of sample shortened links with various timestamps and click counts
     * to demonstrate the UI in different states.
     *
     * @return List of sample ShortenedLink objects
     */
    fun getSampleLinks(): List<ShortenedLink> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            ShortenedLink(
                id = "1",
                originalUrl = "https://developer.android.com/jetpack/compose/documentation",
                shortUrl = "https://lnk.mn/aB3dEf",
                alias = "aB3dEf",
                timestamp = currentTime - 3600000, // 1 hour ago
                clickCount = 42
            ),
            ShortenedLink(
                id = "2",
                originalUrl = "https://github.com/android/architecture-samples",
                shortUrl = "https://lnk.mn/gH7iJk",
                alias = "gH7iJk",
                timestamp = currentTime - 7200000, // 2 hours ago
                clickCount = 28
            ),
            ShortenedLink(
                id = "3",
                originalUrl = "https://material.io/design/introduction",
                shortUrl = "https://lnk.mn/mN9oPq",
                alias = "mN9oPq",
                timestamp = currentTime - 86400000, // 1 day ago
                clickCount = 156
            ),
            ShortenedLink(
                id = "4",
                originalUrl = "https://kotlinlang.org/docs/coroutines-overview.html",
                shortUrl = "https://lnk.mn/rS5tUv",
                alias = "rS5tUv",
                timestamp = currentTime - 172800000, // 2 days ago
                clickCount = 89
            ),
            ShortenedLink(
                id = "5",
                originalUrl = "https://square.github.io/retrofit/",
                shortUrl = "https://lnk.mn/wX1yZa",
                alias = "wX1yZa",
                timestamp = currentTime - 259200000, // 3 days ago
                clickCount = 64
            )
        )
    }

    /**
     * Validates whether a given string is a valid URL.
     *
     * Checks if the input string is not blank and starts with either http:// or https://
     * protocol, and has a minimum length to be considered a valid URL.
     *
     * @param url The URL string to validate
     * @return true if the URL is valid, false otherwise
     */
    fun isValidUrl(url: String): Boolean {
        return url.isNotBlank() &&
                (url.startsWith("http://", ignoreCase = true) ||
                        url.startsWith("https://", ignoreCase = true)) &&
                url.length > 10
    }
}