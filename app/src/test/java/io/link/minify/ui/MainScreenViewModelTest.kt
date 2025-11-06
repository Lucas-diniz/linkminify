package io.link.minify.ui

import io.link.minify.R
import io.link.minify.domain.NetWorkResult
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.error.LinkError
import io.link.minify.domain.error.NetworkError
import io.link.minify.domain.useCase.ListResentLinksUseCase
import io.link.minify.domain.useCase.ShortenLinkUseCase
import io.link.minify.ui.mainScreen.MainScreenViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val listResentLinksUseCase: ListResentLinksUseCase = mockk()
    private val shortenLinkUseCase: ShortenLinkUseCase = mockk()
    private lateinit var mainScreenViewModel: MainScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { listResentLinksUseCase() } returns flowOf(emptyList())
        mainScreenViewModel =
            MainScreenViewModel(
                listResentLinksUseCase = listResentLinksUseCase,
                shortenLinkUseCase = shortenLinkUseCase,
            )
    }

    @After
    fun setDown() {
        Dispatchers.resetMain()
        confirmVerified(
            listResentLinksUseCase,
            shortenLinkUseCase,
        )
        unmockkAll()
    }

    @Test
    fun shortenLink() {
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

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.success_message,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkMissingFields() {
        val network = NetWorkResult.Error(NetworkError.MissingFields)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.missing_fields,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkUnknown() {
        val network = NetWorkResult.Error(NetworkError.Unknown)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.error_unknown,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkNotFound() {
        val network = NetWorkResult.Error(NetworkError.NotFound)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.error_not_found,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkTimeout() {
        val network = NetWorkResult.Error(NetworkError.Timeout)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.error_timeout,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkServerError() {
        val network = NetWorkResult.Error(NetworkError.ServerError)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.error_server,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkNetworkUnavailable() {
        val network = NetWorkResult.Error(NetworkError.NetworkUnavailable)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.error_no_internet,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkAlreadyExists() {
        val network = NetWorkResult.Error(LinkError.AlreadyExists)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.error_link_already_exists,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkMalformed() {
        val network = NetWorkResult.Error(LinkError.Malformed)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.malformed,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkEmptyHost() {
        val network = NetWorkResult.Error(LinkError.EmptyHost)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.empty_host,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkInvalidScheme() {
        val network = NetWorkResult.Error(LinkError.InvalidScheme)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.invalid_scheme,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun shortenLinkEmpty() {
        val network = NetWorkResult.Error(LinkError.Empty)

        coEvery { shortenLinkUseCase.invoke(any()) } returns network

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.url_is_empty,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun clearErrorMessage() {
        mainScreenViewModel.clearErrorMessage()

        coVerify(exactly = 1) { listResentLinksUseCase() }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            null,
        )
    }

    @Test
    fun shortenLinkUnexpectedException() {
        coEvery { shortenLinkUseCase.invoke(any()) } throws RuntimeException("Unexpected error")

        mainScreenViewModel.shortenLink("https://www.google.com/")

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { shortenLinkUseCase("https://www.google.com/") }

        assertEquals(
            mainScreenViewModel.uiState.value.message,
            R.string.shortenLink_error,
        )
        assertFalse(mainScreenViewModel.uiState.value.isLoading)
    }

    @Test
    fun loadResentLinksUpdatesState() {
        val link1 =
            MinifyLink
                .create(
                    id = UUID.randomUUID().toString(),
                    url = "https://www.example.com/",
                    alias = "alias1",
                    shortUrl = "shortUrl1",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        val link2 =
            MinifyLink
                .create(
                    id = UUID.randomUUID().toString(),
                    url = "https://www.test.com/",
                    alias = "alias2",
                    shortUrl = "shortUrl2",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        val linksList = listOf(link1, link2)

        val listResentLinksUseCaseLocal: ListResentLinksUseCase = mockk()
        val shortenLinkUseCaseLocal: ShortenLinkUseCase = mockk()

        coEvery { listResentLinksUseCaseLocal() } returns flowOf(linksList)

        val viewModel =
            MainScreenViewModel(
                listResentLinksUseCase = listResentLinksUseCaseLocal,
                shortenLinkUseCase = shortenLinkUseCaseLocal,
            )

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { listResentLinksUseCaseLocal() }

        assertEquals(
            viewModel.uiState.value.listShortLinks,
            linksList,
        )
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun loadResentLinksUnexpectedException() {
        val listResentLinksUseCaseLocal: ListResentLinksUseCase = mockk()
        val shortenLinkUseCaseLocal: ShortenLinkUseCase = mockk()

        coEvery { listResentLinksUseCaseLocal() } throws RuntimeException("Unexpected error")

        val viewModel =
            MainScreenViewModel(
                listResentLinksUseCase = listResentLinksUseCaseLocal,
                shortenLinkUseCase = shortenLinkUseCaseLocal,
            )

        coVerify(exactly = 1) { listResentLinksUseCase() }
        coVerify(exactly = 1) { listResentLinksUseCaseLocal() }

        assertEquals(
            viewModel.uiState.value.message,
            R.string.list_error,
        )
        assertFalse(viewModel.uiState.value.isLoading)
    }
}
