package io.link.minify.domain.useCase

import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.repository.LinksRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ListResentLinksUseCaseTest {
    private val repository: LinksRepository = mockk()
    private val listResentLinksUseCase: ListResentLinksUseCase = ListResentLinksUseCase(repository)

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
    }

    @After
    fun setDown() {
        confirmVerified(repository)
        unmockkAll()
    }

    @Test
    fun testInvokeShouldReturnEmptyList(): Unit =
        runBlocking {
            every { repository.getResentLinks() } returns flowOf(emptyList())

            val result = listResentLinksUseCase.invoke().first()

            verify(exactly = 1) { repository.getResentLinks() }
            assertEquals(emptyList<MinifyLink>(), result)
        }

    @Test
    fun testInvokeShouldReturnSingleLink(): Unit =
        runBlocking {
            val links = listOf(link1)
            every { repository.getResentLinks() } returns flowOf(links)

            val result = listResentLinksUseCase.invoke().first()

            verify(exactly = 1) { repository.getResentLinks() }
            assertEquals(1, result.size)
            assertEquals(link1, result[0])
        }

    @Test
    fun testInvokeShouldReturnMultipleLinks(): Unit =
        runBlocking {
            val links = listOf(link1, link2, link3)
            every { repository.getResentLinks() } returns flowOf(links)

            val result = listResentLinksUseCase.invoke().first()

            verify(exactly = 1) { repository.getResentLinks() }
            assertEquals(3, result.size)
            assertEquals(link1, result[0])
            assertEquals(link2, result[1])
            assertEquals(link3, result[2])
        }

    @Test
    fun testInvokeShouldLimitTo20Links(): Unit =
        runBlocking {
            val links =
                (1..25).map { index ->
                    MinifyLink
                        .create(
                            id = index.toString(),
                            url = "https://www.example$index.com",
                            alias = "alias$index",
                            shortUrl = "https://short.ly/alias$index",
                            timestamp = System.currentTimeMillis() - (index * 1000L),
                        ).getOrThrow()
                }
            every { repository.getResentLinks() } returns flowOf(links)

            val result = listResentLinksUseCase.invoke().first()

            verify(exactly = 1) { repository.getResentLinks() }
            assertEquals(20, result.size)
            assertEquals(links[0], result[0])
            assertEquals(links[19], result[19])
        }

    @Test
    fun testInvokeShouldReturnExactly20LinksWhenExactly20Available(): Unit =
        runBlocking {
            val links =
                (1..20).map { index ->
                    MinifyLink
                        .create(
                            id = index.toString(),
                            url = "https://www.example$index.com",
                            alias = "alias$index",
                            shortUrl = "https://short.ly/alias$index",
                            timestamp = System.currentTimeMillis(),
                        ).getOrThrow()
                }
            every { repository.getResentLinks() } returns flowOf(links)

            val result = listResentLinksUseCase.invoke().first()

            verify(exactly = 1) { repository.getResentLinks() }
            assertEquals(20, result.size)
        }

    @Test
    fun testInvokeShouldReturnLessThan20LinksWhenLessAvailable(): Unit =
        runBlocking {
            val links =
                (1..10).map { index ->
                    MinifyLink
                        .create(
                            id = index.toString(),
                            url = "https://www.example$index.com",
                            alias = "alias$index",
                            shortUrl = "https://short.ly/alias$index",
                            timestamp = System.currentTimeMillis(),
                        ).getOrThrow()
                }
            every { repository.getResentLinks() } returns flowOf(links)

            val result = listResentLinksUseCase.invoke().first()

            verify(exactly = 1) { repository.getResentLinks() }
            assertEquals(10, result.size)
        }

    @Test
    fun testInvokeShouldTakeFirst20LinksInOrder(): Unit =
        runBlocking {
            val links =
                (1..30).map { index ->
                    MinifyLink
                        .create(
                            id = index.toString(),
                            url = "https://www.example$index.com",
                            alias = "alias$index",
                            shortUrl = "https://short.ly/alias$index",
                            timestamp = System.currentTimeMillis() - (index * 1000L),
                        ).getOrThrow()
                }
            every { repository.getResentLinks() } returns flowOf(links)

            val result = listResentLinksUseCase.invoke().first()

            verify(exactly = 1) { repository.getResentLinks() }
            assertEquals(20, result.size)
            assertEquals("alias1", result[0].alias)
            assertEquals("alias20", result[19].alias)
        }

    @Test
    fun testInvokeShouldReturnFlowThatCanBeCollectedMultipleTimes(): Unit =
        runBlocking {
            val links = listOf(link1, link2)
            every { repository.getResentLinks() } returns flowOf(links)

            val result1 = listResentLinksUseCase.invoke().first()
            val result2 = listResentLinksUseCase.invoke().first()

            verify(exactly = 2) { repository.getResentLinks() }
            assertEquals(result1, result2)
            assertEquals(2, result1.size)
        }
}
