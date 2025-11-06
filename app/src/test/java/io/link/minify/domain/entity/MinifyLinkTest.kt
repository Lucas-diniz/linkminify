package io.link.minify.domain.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MinifyLinkTest {
    private val validId = "123"
    private val validUrl = "https://www.example.com"
    private val validAlias = "abc123"
    private val validShortUrl = "https://short.ly/abc123"
    private val validTimestamp = System.currentTimeMillis()

    @Test
    fun testCreateShouldReturnSuccessWithValidData() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = validTimestamp,
            )

        assertTrue(result.isSuccess)
        val link = result.getOrThrow()
        assertEquals(validId, link.id)
        assertEquals(validUrl, link.url)
        assertEquals(validAlias, link.alias)
        assertEquals(validShortUrl, link.shortUrl)
        assertEquals(validTimestamp, link.timestamp)
    }

    @Test
    fun testCreateShouldFailWithBlankId() {
        val result =
            MinifyLink.create(
                id = "",
                url = validUrl,
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("ID cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithWhitespaceId() {
        val result =
            MinifyLink.create(
                id = "   ",
                url = validUrl,
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("ID cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithBlankUrl() {
        val result =
            MinifyLink.create(
                id = validId,
                url = "",
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("URL cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithWhitespaceUrl() {
        val result =
            MinifyLink.create(
                id = validId,
                url = "   ",
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("URL cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithBlankAlias() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = "",
                shortUrl = validShortUrl,
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("Alias cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithWhitespaceAlias() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = "   ",
                shortUrl = validShortUrl,
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("Alias cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithBlankShortUrl() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = validAlias,
                shortUrl = "",
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("Short URL cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithWhitespaceShortUrl() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = validAlias,
                shortUrl = "   ",
                timestamp = validTimestamp,
            )

        assertTrue(result.isFailure)
        assertEquals("Short URL cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithZeroTimestamp() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = 0,
            )

        assertTrue(result.isFailure)
        assertEquals("Timestamp must be positive", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldFailWithNegativeTimestamp() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = -1000,
            )

        assertTrue(result.isFailure)
        assertEquals("Timestamp must be positive", result.exceptionOrNull()?.message)
    }

    @Test
    fun testCreateShouldSucceedWithMinimumPositiveTimestamp() {
        val result =
            MinifyLink.create(
                id = validId,
                url = validUrl,
                alias = validAlias,
                shortUrl = validShortUrl,
                timestamp = 1,
            )

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().timestamp)
    }

    @Test
    fun testHasSameUrlAsShouldReturnTrueForExactMatch() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertTrue(link.hasSameUrlAs("https://www.example.com"))
    }

    @Test
    fun testHasSameUrlAsShouldReturnTrueForCaseInsensitiveMatch() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertTrue(link.hasSameUrlAs("HTTPS://WWW.EXAMPLE.COM"))
    }

    @Test
    fun testHasSameUrlAsShouldReturnTrueForMixedCaseMatch() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertTrue(link.hasSameUrlAs("HtTpS://WwW.ExAmPlE.CoM"))
    }

    @Test
    fun testHasSameUrlAsShouldReturnTrueWhenUrlHasLeadingWhitespace() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertTrue(link.hasSameUrlAs("   https://www.example.com"))
    }

    @Test
    fun testHasSameUrlAsShouldReturnTrueWhenUrlHasTrailingWhitespace() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertTrue(link.hasSameUrlAs("https://www.example.com   "))
    }

    @Test
    fun testHasSameUrlAsShouldReturnTrueWhenBothHaveWhitespace() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "  https://www.example.com  ",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertTrue(link.hasSameUrlAs("   https://www.example.com   "))
    }

    @Test
    fun testHasSameUrlAsShouldReturnFalseForDifferentUrl() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertFalse(link.hasSameUrlAs("https://www.different.com"))
    }

    @Test
    fun testHasSameUrlAsShouldReturnFalseForDifferentPath() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com/path1",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertFalse(link.hasSameUrlAs("https://www.example.com/path2"))
    }

    @Test
    fun testHasSameUrlAsShouldReturnFalseForEmptyString() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = "https://www.example.com",
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertFalse(link.hasSameUrlAs(""))
    }

    @Test
    fun testDataClassEqualityShouldReturnTrueForSameValues() {
        val link1 =
            MinifyLink
                .create(
                    id = validId,
                    url = validUrl,
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        val link2 =
            MinifyLink
                .create(
                    id = validId,
                    url = validUrl,
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertEquals(link1, link2)
    }

    @Test
    fun testDataClassEqualityShouldReturnFalseForDifferentIds() {
        val link1 =
            MinifyLink
                .create(
                    id = "123",
                    url = validUrl,
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        val link2 =
            MinifyLink
                .create(
                    id = "456",
                    url = validUrl,
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertFalse(link1 == link2)
    }

    @Test
    fun testDataClassHashCodeShouldBeConsistent() {
        val link =
            MinifyLink
                .create(
                    id = validId,
                    url = validUrl,
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        val hashCode1 = link.hashCode()
        val hashCode2 = link.hashCode()

        assertEquals(hashCode1, hashCode2)
    }

    @Test
    fun testDataClassHashCodeShouldBeSameForEqualObjects() {
        val link1 =
            MinifyLink
                .create(
                    id = validId,
                    url = validUrl,
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        val link2 =
            MinifyLink
                .create(
                    id = validId,
                    url = validUrl,
                    alias = validAlias,
                    shortUrl = validShortUrl,
                    timestamp = validTimestamp,
                ).getOrThrow()

        assertEquals(link1.hashCode(), link2.hashCode())
    }
}
