package io.link.minify.data.repository

import io.link.minify.data.sources.LocalDataSource
import io.link.minify.data.sources.RemoteDataSource
import io.link.minify.data.sources.model.AliasLinks
import io.link.minify.data.sources.model.CreateAliasRequest
import io.link.minify.data.sources.model.CreateAliasResponse
import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.NetworkError
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class LinksDefaultRepositoryTest {
    private val localDataSource: LocalDataSource = mockk()
    private val remoteDataSource: RemoteDataSource = mockk()
    private lateinit var linksDefaultRepository: LinksDefaultRepository

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

    @Before
    fun setUp() {
        linksDefaultRepository =
            LinksDefaultRepository(
                localDataSource,
                remoteDataSource,
            )
    }

    @After
    fun tearDown() {
        confirmVerified(localDataSource, remoteDataSource)
        unmockkAll()
    }

    @Test
    fun testGetResentLinksShouldDelegateToLocalDataSource(): Unit =
        runBlocking {
            val links = listOf(link1, link2)
            every { localDataSource.getResentLinks() } returns flowOf(links)

            val result = linksDefaultRepository.getResentLinks().first()

            verify(exactly = 1) { localDataSource.getResentLinks() }
            assertEquals(2, result.size)
            assertEquals(link1, result[0])
            assertEquals(link2, result[1])
        }

    @Test
    fun testGetResentLinksShouldReturnEmptyList(): Unit =
        runBlocking {
            every { localDataSource.getResentLinks() } returns flowOf(emptyList())

            val result = linksDefaultRepository.getResentLinks().first()

            verify(exactly = 1) { localDataSource.getResentLinks() }
            assertEquals(emptyList<MinifyLink>(), result)
        }

    @Test
    fun testVerifyIfUrlExistsShouldDelegateToLocalDataSource() {
        every { localDataSource.verifyLinkExists("https://www.example.com") } returns true

        val result = linksDefaultRepository.verifyIfUrlExists("https://www.example.com")

        verify(exactly = 1) { localDataSource.verifyLinkExists("https://www.example.com") }
        assertTrue(result)
    }

    @Test
    fun testVerifyIfUrlExistsShouldReturnFalseWhenLinkDoesNotExist() {
        every { localDataSource.verifyLinkExists("https://www.example.com") } returns false

        val result = linksDefaultRepository.verifyIfUrlExists("https://www.example.com")

        verify(exactly = 1) { localDataSource.verifyLinkExists("https://www.example.com") }
        assertFalse(result)
    }

    @Test
    fun testSaveMinifyLinkShouldDelegateToLocalDataSource() {
        every { localDataSource.saveMinifyLink(link1) } just Runs

        linksDefaultRepository.saveMinifyLink(link1)

        verify(exactly = 1) { localDataSource.saveMinifyLink(link1) }
    }

    @Test
    fun testCreateShortLinkShouldReturnSuccessWhenApiResponseIsValid(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = "abc123",
                    links =
                        AliasLinks(
                            self = "https://www.example.com",
                            short = "https://short.ly/abc123",
                        ),
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Success)
            val link = (result as NetWorkResult.Success).data
            assertEquals("https://www.example.com", link.url)
            assertEquals("abc123", link.alias)
            assertEquals("https://short.ly/abc123", link.shortUrl)
        }

    @Test
    fun testCreateShortLinkShouldReturnErrorWhenAliasIsNull(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = null,
                    links =
                        AliasLinks(
                            self = "https://www.example.com",
                            short = "https://short.ly/abc123",
                        ),
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
            assertEquals(NetworkError.MissingFields, (result as NetWorkResult.Error).error)
        }

    @Test
    fun testCreateShortLinkShouldReturnErrorWhenLinksIsNull(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = "abc123",
                    links = null,
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
            assertEquals(NetworkError.MissingFields, (result as NetWorkResult.Error).error)
        }

    @Test
    fun testCreateShortLinkShouldReturnErrorWhenSelfIsNull(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = "abc123",
                    links =
                        AliasLinks(
                            self = null,
                            short = "https://short.ly/abc123",
                        ),
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
            assertEquals(NetworkError.MissingFields, (result as NetWorkResult.Error).error)
        }

    @Test
    fun testCreateShortLinkShouldReturnErrorWhenShortIsNull(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = "abc123",
                    links =
                        AliasLinks(
                            self = "https://www.example.com",
                            short = null,
                        ),
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
            assertEquals(NetworkError.MissingFields, (result as NetWorkResult.Error).error)
        }

    @Test
    fun testCreateShortLinkShouldReturnErrorWhenAllFieldsAreNull(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = null,
                    links = null,
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
            assertEquals(NetworkError.MissingFields, (result as NetWorkResult.Error).error)
        }

    @Test
    fun testCreateShortLinkShouldReturnErrorWhenExceptionIsThrown(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } throws IOException("Network error")

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
        }

    @Test
    fun testCreateShortLinkShouldReturnErrorWhenMinifyLinkCreateFails(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = "",
                    links =
                        AliasLinks(
                            self = "https://www.example.com",
                            short = "https://short.ly/abc123",
                        ),
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
            assertEquals(NetworkError.MissingFields, (result as NetWorkResult.Error).error)
        }

    @Test
    fun testCreateShortLinkShouldGenerateUniqueIdForEachCall(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = "abc123",
                    links =
                        AliasLinks(
                            self = "https://www.example.com",
                            short = "https://short.ly/abc123",
                        ),
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result1 = linksDefaultRepository.createShortLink(url)
            val result2 = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 2) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result1 is NetWorkResult.Success)
            assertTrue(result2 is NetWorkResult.Success)

            val link1 = (result1 as NetWorkResult.Success).data
            val link2 = (result2 as NetWorkResult.Success).data

            assertTrue(link1.id != link2.id)
        }

    @Test
    fun testCreateShortLinkShouldHandleEmptyStringsInResponse(): Unit =
        runBlocking {
            val url = "https://www.example.com"
            val apiResponse =
                CreateAliasResponse(
                    alias = "",
                    links =
                        AliasLinks(
                            self = "",
                            short = "",
                        ),
                )
            coEvery { remoteDataSource.createAlias(CreateAliasRequest(url)) } returns apiResponse

            val result = linksDefaultRepository.createShortLink(url)

            coVerify(exactly = 1) { remoteDataSource.createAlias(CreateAliasRequest(url)) }
            assertTrue(result is NetWorkResult.Error)
            assertEquals(NetworkError.MissingFields, (result as NetWorkResult.Error).error)
        }

    @Test
    fun testSaveMinifyLinkShouldSaveMultipleLinks() {
        every { localDataSource.saveMinifyLink(any()) } just Runs

        linksDefaultRepository.saveMinifyLink(link1)
        linksDefaultRepository.saveMinifyLink(link2)

        verify(exactly = 1) { localDataSource.saveMinifyLink(link1) }
        verify(exactly = 1) { localDataSource.saveMinifyLink(link2) }
    }

    @Test
    fun testVerifyIfUrlExistsShouldHandleDifferentUrls() {
        every { localDataSource.verifyLinkExists("https://www.example1.com") } returns true
        every { localDataSource.verifyLinkExists("https://www.example2.com") } returns false

        val result1 = linksDefaultRepository.verifyIfUrlExists("https://www.example1.com")
        val result2 = linksDefaultRepository.verifyIfUrlExists("https://www.example2.com")

        verify(exactly = 1) { localDataSource.verifyLinkExists("https://www.example1.com") }
        verify(exactly = 1) { localDataSource.verifyLinkExists("https://www.example2.com") }
        assertTrue(result1)
        assertFalse(result2)
    }
}
