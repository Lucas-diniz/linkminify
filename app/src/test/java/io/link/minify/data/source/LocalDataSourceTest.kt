package io.link.minify.data.source

import io.link.minify.data.sources.LocalDataSource
import io.link.minify.domain.entity.MinifyLink
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocalDataSourceTest {
    private lateinit var localDataSource: LocalDataSource

    private val link1 =
        MinifyLink
            .create(
                id = "1",
                url = "https://www.example1.com",
                alias = "alias1",
                shortUrl = "https://short.ly/alias1",
                timestamp = System.currentTimeMillis(),
            ).getOrThrow()

    private val link2 =
        MinifyLink
            .create(
                id = "2",
                url = "https://www.example2.com",
                alias = "alias2",
                shortUrl = "https://short.ly/alias2",
                timestamp = System.currentTimeMillis(),
            ).getOrThrow()

    private val link3 =
        MinifyLink
            .create(
                id = "3",
                url = "https://www.example3.com",
                alias = "alias3",
                shortUrl = "https://short.ly/alias3",
                timestamp = System.currentTimeMillis(),
            ).getOrThrow()

    @Before
    fun setUp() {
        localDataSource = LocalDataSource()
    }

    @Test
    fun testGetResentLinksShouldReturnEmptyListInitially(): Unit =
        runBlocking {
            val result = localDataSource.getResentLinks().first()

            assertEquals(emptyList<MinifyLink>(), result)
        }

    @Test
    fun testSaveMinifyLinkShouldAddLinkToStorage(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.getResentLinks().first()

            assertEquals(1, result.size)
            assertEquals(link1, result[0])
        }

    @Test
    fun testSaveMinifyLinkShouldAddLinkAtBeginning(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)
            localDataSource.saveMinifyLink(link2)

            val result = localDataSource.getResentLinks().first()

            assertEquals(2, result.size)
            assertEquals(link2, result[0])
            assertEquals(link1, result[1])
        }

    @Test
    fun testSaveMinifyLinkShouldAddMultipleLinksInReverseOrder(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)
            localDataSource.saveMinifyLink(link2)
            localDataSource.saveMinifyLink(link3)

            val result = localDataSource.getResentLinks().first()

            assertEquals(3, result.size)
            assertEquals(link3, result[0])
            assertEquals(link2, result[1])
            assertEquals(link1, result[2])
        }

    @Test
    fun testSaveMinifyLinkShouldEmitNewValueToFlow(): Unit =
        runBlocking {
            val initialResult = localDataSource.getResentLinks().first()
            assertEquals(0, initialResult.size)

            localDataSource.saveMinifyLink(link1)

            val updatedResult = localDataSource.getResentLinks().first()
            assertEquals(1, updatedResult.size)
            assertEquals(link1, updatedResult[0])
        }

    @Test
    fun testVerifyLinkExistsShouldReturnFalseWhenStorageIsEmpty(): Unit =
        runBlocking {
            val result = localDataSource.verifyLinkExists("https://www.example.com")

            assertFalse(result)
        }

    @Test
    fun testVerifyLinkExistsShouldReturnTrueWhenLinkExists(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.verifyLinkExists("https://www.example1.com")

            assertTrue(result)
        }

    @Test
    fun testVerifyLinkExistsShouldReturnFalseWhenLinkDoesNotExist(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.verifyLinkExists("https://www.different.com")

            assertFalse(result)
        }

    @Test
    fun testVerifyLinkExistsShouldBeCaseInsensitive(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.verifyLinkExists("HTTPS://WWW.EXAMPLE1.COM")

            assertTrue(result)
        }

    @Test
    fun testVerifyLinkExistsShouldIgnoreWhitespace(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.verifyLinkExists("  https://www.example1.com  ")

            assertTrue(result)
        }

    @Test
    fun testVerifyLinkExistsShouldHandleMultipleLinks(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)
            localDataSource.saveMinifyLink(link2)
            localDataSource.saveMinifyLink(link3)

            assertTrue(localDataSource.verifyLinkExists("https://www.example1.com"))
            assertTrue(localDataSource.verifyLinkExists("https://www.example2.com"))
            assertTrue(localDataSource.verifyLinkExists("https://www.example3.com"))
            assertFalse(localDataSource.verifyLinkExists("https://www.example4.com"))
        }

    @Test
    fun testGetResentLinksShouldReturnFlowThatCanBeCollectedMultipleTimes(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result1 = localDataSource.getResentLinks().first()
            val result2 = localDataSource.getResentLinks().first()

            assertEquals(result1, result2)
            assertEquals(1, result1.size)
        }

    @Test
    fun testSaveMinifyLinkShouldNotModifyOriginalList(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)
            val firstResult = localDataSource.getResentLinks().first()

            localDataSource.saveMinifyLink(link2)
            val secondResult = localDataSource.getResentLinks().first()

            assertEquals(1, firstResult.size)
            assertEquals(2, secondResult.size)
        }

    @Test
    fun testGetResentLinksShouldReturnImmutableSnapshot(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)
            localDataSource.saveMinifyLink(link2)

            val result = localDataSource.getResentLinks().first()
            assertEquals(2, result.size)

            localDataSource.saveMinifyLink(link3)

            assertEquals(2, result.size)

            val newResult = localDataSource.getResentLinks().first()
            assertEquals(3, newResult.size)
        }

    @Test
    fun testSaveMinifyLinkShouldAllowDuplicateLinks(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.getResentLinks().first()

            assertEquals(2, result.size)
            assertEquals(link1, result[0])
            assertEquals(link1, result[1])
        }

    @Test
    fun testVerifyLinkExistsShouldReturnTrueWhenDuplicatesExist(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.verifyLinkExists("https://www.example1.com")

            assertTrue(result)
        }

    @Test
    fun testSaveMinifyLinkAndVerifyInSequence(): Unit =
        runBlocking {
            assertFalse(localDataSource.verifyLinkExists("https://www.example1.com"))

            localDataSource.saveMinifyLink(link1)
            assertTrue(localDataSource.verifyLinkExists("https://www.example1.com"))

            localDataSource.saveMinifyLink(link2)
            assertTrue(localDataSource.verifyLinkExists("https://www.example1.com"))
            assertTrue(localDataSource.verifyLinkExists("https://www.example2.com"))
            assertFalse(localDataSource.verifyLinkExists("https://www.example3.com"))
        }

    @Test
    fun testFlowEmitsUpdatesWhenMultipleLinksAdded(): Unit =
        runBlocking {
            var emissionCount = 0
            val emissions = mutableListOf<List<MinifyLink>>()

            val initialValue = localDataSource.getResentLinks().first()
            emissions.add(initialValue)
            emissionCount++

            localDataSource.saveMinifyLink(link1)
            val afterFirst = localDataSource.getResentLinks().first()
            emissions.add(afterFirst)
            emissionCount++

            localDataSource.saveMinifyLink(link2)
            val afterSecond = localDataSource.getResentLinks().first()
            emissions.add(afterSecond)
            emissionCount++

            assertEquals(3, emissionCount)
            assertEquals(0, emissions[0].size)
            assertEquals(1, emissions[1].size)
            assertEquals(2, emissions[2].size)
        }

    @Test
    fun testVerifyLinkExistsWithEmptyString(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.verifyLinkExists("")

            assertFalse(result)
        }

    @Test
    fun testVerifyLinkExistsWithOnlyWhitespace(): Unit =
        runBlocking {
            localDataSource.saveMinifyLink(link1)

            val result = localDataSource.verifyLinkExists("   ")

            assertFalse(result)
        }
}
