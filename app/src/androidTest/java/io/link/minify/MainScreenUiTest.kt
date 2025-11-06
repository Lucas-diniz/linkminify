package io.link.minify

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.link.minify.domain.entity.MinifyLink
import io.link.minify.domain.useCase.ListResentLinksUseCase
import io.link.minify.domain.useCase.ShortenLinkUseCase
import io.link.minify.ui.mainScreen.MainScreen
import io.link.minify.ui.mainScreen.MainScreenViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeRepository: LinksFakeRepository
    private lateinit var listResentLinksUseCase: ListResentLinksUseCase
    private lateinit var shortenLinkUseCase: ShortenLinkUseCase
    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setup() {
        fakeRepository = LinksFakeRepository()
        listResentLinksUseCase = ListResentLinksUseCase(fakeRepository)
        shortenLinkUseCase = ShortenLinkUseCase(fakeRepository)
    }

    private fun setContentWithViewModel() {
        viewModel = MainScreenViewModel(listResentLinksUseCase, shortenLinkUseCase)
        composeTestRule.setContent {
            MainScreen(mainScreenViewModel = viewModel)
        }
    }

    @Test
    fun screenDisplaysAppTitleCorrectly() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("app_title")
            .assertIsDisplayed()
            .assertTextContains("Link Minify")
    }

    @Test
    fun screenContainerIsDisplayed() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("link_shortener_screen")
            .assertIsDisplayed()
    }

    @Test
    fun inputSectionIsVisibleOnScreenLoad() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("input_section")
            .assertIsDisplayed()
    }

    @Test
    fun urlInputFieldIsVisibleAndEnabledByDefault() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun shortenButtonIsVisibleButDisabledWhenInputIsEmpty() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("shorten_button")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun shortenButtonIsEnabledWhenValidUrlIsEntered() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("https://www.example.com")

        composeTestRule
            .onNodeWithTag("shorten_button")
            .assertIsEnabled()
    }

    @Test
    fun shortenButtonRemainsDisabledWhenInvalidUrlIsEntered() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("not-a-valid-url")

        composeTestRule
            .onNodeWithTag("shorten_button")
            .assertIsNotEnabled()
    }

    @Test
    fun errorTextIsDisplayedForInvalidUrl() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("invalid-url")

        composeTestRule
            .onNodeWithTag("url_error_text")
            .assertIsDisplayed()
    }

    @Test
    fun emptyStateIsDisplayedWhenNoLinksExist() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("empty_state")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("empty_state_title")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("empty_state_subtitle")
            .assertIsDisplayed()
    }

    @Test
    fun emptyStateIsNotDisplayedWhenLinksExist() {
        val testLink =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://www.example.com",
                    alias = "abc123",
                    shortUrl = "https://lnk.mn/abc123",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.setLinks(listOf(testLink))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("empty_state")
            .assertDoesNotExist()
    }

    @Test
    fun recentLinksHeaderIsDisplayedWhenLinksExist() {
        val testLink =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://www.example.com",
                    alias = "abc123",
                    shortUrl = "https://lnk.mn/abc123",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.setLinks(listOf(testLink))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("recent_links_header")
            .assertIsDisplayed()
    }

    @Test
    fun recentLinksHeaderIsNotDisplayedWhenNoLinksExist() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("recent_links_header")
            .assertDoesNotExist()
    }

    @Test
    fun singleLinkItemIsDisplayedCorrectly() {
        val testLink =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://www.example.com",
                    alias = "abc123",
                    shortUrl = "https://lnk.mn/abc123",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.setLinks(listOf(testLink))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("link_item_abc123")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithTag("original_url_abc123", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextContains("https://www.example.com")

        composeTestRule
            .onNodeWithTag("short_url_abc123", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextContains("https://lnk.mn/abc123")
    }

    @Test
    fun multipleLinkItemsAreDisplayedCorrectly() {
        val link1 =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://www.example1.com",
                    alias = "abc123",
                    shortUrl = "https://lnk.mn/abc123",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        val link2 =
            MinifyLink
                .create(
                    id = "2",
                    url = "https://www.example2.com",
                    alias = "def456",
                    shortUrl = "https://lnk.mn/def456",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        val link3 =
            MinifyLink
                .create(
                    id = "3",
                    url = "https://www.example3.com",
                    alias = "ghi789",
                    shortUrl = "https://lnk.mn/ghi789",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.setLinks(listOf(link1, link2, link3))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("link_item_abc123")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("link_item_def456")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("link_item_ghi789")
            .assertIsDisplayed()
    }

    @Test
    fun loadingOverlayIsNotDisplayedInitiallyWhenLinksExist() {
        val testLink =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://www.example.com",
                    alias = "abc123",
                    shortUrl = "https://lnk.mn/abc123",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.setLinks(listOf(testLink))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("loading_overlay")
            .assertIsNotDisplayed()
    }

    @Test
    fun loadingOverlayAppearsWhenShorteningLink() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("https://www.example.com")

        composeTestRule
            .onNodeWithTag("shorten_button")
            .performClick()

        composeTestRule
            .onNodeWithTag("loading_overlay")
            .assertIsDisplayed()
    }

    @Test
    fun inputFieldIsDisabledDuringLoading() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("https://www.example.com")

        composeTestRule
            .onNodeWithTag("shorten_button")
            .performClick()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .assertIsNotEnabled()
    }

    @Test
    fun shortenButtonIsDisabledDuringLoading() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("https://www.example.com")

        composeTestRule
            .onNodeWithTag("shorten_button")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("shorten_button")
            .assertIsNotEnabled()
    }

    @Test
    fun newLinkAppearsInListAfterSuccessfulShortening() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("https://www.newexample.com")

        composeTestRule
            .onNodeWithTag("shorten_button")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("recent_links_header")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("empty_state")
            .assertDoesNotExist()
    }

    @Test
    fun linksListScrollsProperly() {
        val links =
            (1..5).map { index ->
                MinifyLink
                    .create(
                        id = index.toString(),
                        url = "https://www.example$index.com",
                        alias = "link$index",
                        shortUrl = "https://lnk.mn/link$index",
                        timestamp = System.currentTimeMillis() - (index * 1000L),
                    ).getOrThrow()
            }

        fakeRepository.setLinks(links)
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("links_list")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("link_item_link1")
            .assertIsDisplayed()
    }

    @Test
    fun timestampIsDisplayedForEachLinkItem() {
        val testLink =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://www.example.com",
                    alias = "abc123",
                    shortUrl = "https://lnk.mn/abc123",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.setLinks(listOf(testLink))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("link_timestamp_abc123")
            .assertIsDisplayed()
    }

    @Test
    fun emptyStateShowsCorrectTitleText() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("empty_state_title")
            .assertIsDisplayed()
    }

    @Test
    fun emptyStateShowsCorrectSubtitleText() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("empty_state_subtitle")
            .assertIsDisplayed()
    }

    @Test
    fun buttonTextChangesDuringLoading() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput("https://www.example.com")

        composeTestRule
            .onNodeWithTag("shorten_button")
            .performClick()

        composeTestRule
            .onNodeWithTag("shorten_button")
            .assertTextContains("Shortening")
    }

    @Test
    fun screenHandlesEmptyThenPopulatedStateTransition() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("empty_state")
            .assertIsDisplayed()

        val newLink =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://www.example.com",
                    alias = "abc123",
                    shortUrl = "https://lnk.mn/abc123",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.addLink(newLink)

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("empty_state")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("recent_links_header")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("link_item_abc123")
            .assertIsDisplayed()
    }

    @Test
    fun allLinkItemComponentsAreVisible() {
        val testLink =
            MinifyLink
                .create(
                    id = "test123",
                    url = "https://www.longexampleurl.com/with/path",
                    alias = "short1",
                    shortUrl = "https://lnk.mn/short1",
                    timestamp = System.currentTimeMillis(),
                ).getOrThrow()

        fakeRepository.setLinks(listOf(testLink))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("link_item_short1")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("link_timestamp_short1")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("original_url_short1")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("short_url_short1")
            .assertIsDisplayed()
    }

    @Test
    fun linksAreDisplayedInCorrectOrder() {
        val link1 =
            MinifyLink
                .create(
                    id = "1",
                    url = "https://first.com",
                    alias = "first",
                    shortUrl = "https://lnk.mn/first",
                    timestamp = 1000L,
                ).getOrThrow()

        val link2 =
            MinifyLink
                .create(
                    id = "2",
                    url = "https://second.com",
                    alias = "second",
                    shortUrl = "https://lnk.mn/second",
                    timestamp = 2000L,
                ).getOrThrow()

        fakeRepository.setLinks(listOf(link1, link2))
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("link_item_first")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("link_item_second")
            .assertIsDisplayed()
    }

    @Test
    fun urlInputFieldAcceptsTextInput() {
        fakeRepository.clearLinks()
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        val testUrl = "https://www.testurl.com"

        composeTestRule
            .onNodeWithTag("url_input_field")
            .performTextInput(testUrl)

        composeTestRule
            .onNodeWithTag("url_input_field")
            .assertTextContains(testUrl)
    }

    @Test
    fun screenDisplaysCorrectNumberOfLinks() {
        val links =
            (1..3).map { index ->
                MinifyLink
                    .create(
                        id = index.toString(),
                        url = "https://example$index.com",
                        alias = "alias$index",
                        shortUrl = "https://lnk.mn/alias$index",
                        timestamp = System.currentTimeMillis(),
                    ).getOrThrow()
            }

        fakeRepository.setLinks(links)
        setContentWithViewModel()

        composeTestRule.waitForIdle()

        links.forEach { link ->
            composeTestRule
                .onNodeWithTag("link_item_${link.alias}")
                .assertIsDisplayed()
        }
    }
}
