package io.link.minify.domain.useCase

import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.LinkError
import io.link.minify.domain.error.NetworkError
import io.link.minify.domain.repository.LinksRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ShortenLinkUseCaseTest {
    private val repository: LinksRepository = mockk()
    private val shortenLinkUseCase: ShortenLinkUseCase = ShortenLinkUseCase(repository)

    val link =
        MinifyLink
            .create(
                id = UUID.randomUUID().toString(),
                url = "https://www.google.com/",
                alias = "alias",
                shortUrl = "shortUrl",
                timestamp = System.currentTimeMillis(),
            ).getOrThrow()

    val network = NetWorkResult.Success(link)

    @Before
    fun setUp() {
        coEvery { repository.verifyIfUrlExists(any()) } returns false
        coEvery { repository.createShortLink(any()) } returns network
        coEvery { repository.saveMinifyLink(any()) } just Runs
    }

    @After
    fun setDown() {
        confirmVerified(
            repository,
        )
        unmockkAll()
    }

    @Test
    fun `testInvoke should return success`(): Unit =
        runBlocking {
            val actual = shortenLinkUseCase.invoke("https://www.google.com/")

            coVerify(exactly = 1) { repository.verifyIfUrlExists("https://www.google.com/") }
            coVerify(exactly = 1) { repository.createShortLink("https://www.google.com/") }
            coVerify(exactly = 1) { repository.saveMinifyLink(link) }

            assert(actual == network)
        }

    @Test
    fun `testInvoke should return invalid url`(): Unit =
        runBlocking {
            val actual = shortenLinkUseCase.invoke("www.google.com")

            coVerify(exactly = 0) { repository.verifyIfUrlExists("www.google.com") }
            coVerify(exactly = 0) { repository.createShortLink("https://www.google.com/") }
            coVerify(exactly = 0) { repository.saveMinifyLink(link) }

            assert(actual == NetWorkResult.Error(LinkError.InvalidScheme))
        }

    @Test
    fun `testInvoke should return invalid host`(): Unit =
        runBlocking {
            val actual = shortenLinkUseCase.invoke("https:///")

            coVerify(exactly = 0) { repository.verifyIfUrlExists("www.google.com") }
            coVerify(exactly = 0) { repository.createShortLink("https://www.google.com/") }
            coVerify(exactly = 0) { repository.saveMinifyLink(link) }

            assert(actual == NetWorkResult.Error(LinkError.EmptyHost))
        }

    @Test
    fun `testInvoke should return host host indexOf`(): Unit =
        runBlocking {
            val actual = shortenLinkUseCase.invoke("https://wwwgooglecom/")

            coVerify(exactly = 0) { repository.verifyIfUrlExists("www.google.com") }
            coVerify(exactly = 0) { repository.createShortLink("https://www.google.com/") }
            coVerify(exactly = 0) { repository.saveMinifyLink(link) }

            assert(actual == NetWorkResult.Error(LinkError.Malformed))
        }

    @Test
    fun `testInvoke should return host contains space`(): Unit =
        runBlocking {
            val actual = shortenLinkUseCase.invoke("https://www goog lecom/")

            coVerify(exactly = 0) { repository.verifyIfUrlExists("www.google.com") }
            coVerify(exactly = 0) { repository.createShortLink("https://www.google.com/") }
            coVerify(exactly = 0) { repository.saveMinifyLink(link) }

            assert(actual == NetWorkResult.Error(LinkError.Malformed))
        }

    @Test
    fun `testInvoke should return Url Exists`(): Unit =
        runBlocking {
            coEvery { repository.verifyIfUrlExists(any()) } returns true

            val actual = shortenLinkUseCase.invoke("https://www.google.com/")

            coVerify(exactly = 1) { repository.verifyIfUrlExists("https://www.google.com/") }
            coVerify(exactly = 0) { repository.createShortLink("https://www.google.com/") }
            coVerify(exactly = 0) { repository.saveMinifyLink(link) }

            assert(actual == NetWorkResult.Error(LinkError.AlreadyExists))
        }

    @Test
    fun `testInvoke should return Url MissingFields`(): Unit =
        runBlocking {
            coEvery { repository.createShortLink(any()) } returns NetWorkResult.Error(NetworkError.MissingFields)

            val actual = shortenLinkUseCase.invoke("https://www.google.com/")

            coVerify(exactly = 1) { repository.verifyIfUrlExists("https://www.google.com/") }
            coVerify(exactly = 1) { repository.createShortLink("https://www.google.com/") }
            coVerify(exactly = 0) { repository.saveMinifyLink(link) }

            assert(actual == NetWorkResult.Error(NetworkError.MissingFields))
        }
}
