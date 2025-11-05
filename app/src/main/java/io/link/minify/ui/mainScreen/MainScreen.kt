package io.link.minify.ui.mainScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.link.minify.getFormatTimestamp
import io.link.minify.ui.components.EmptyState
import io.link.minify.ui.components.InputLink
import io.link.minify.ui.components.LoadingOverlay
import io.link.minify.ui.components.ShortenedLinkItem
import org.koin.androidx.compose.koinViewModel


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(mainScreenViewModel: MainScreenViewModel = koinViewModel()) {

    val uiState by mainScreenViewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { errorResId ->
            Toast.makeText(
                context,
                context.getString(errorResId),
                Toast.LENGTH_LONG
            ).show()
            mainScreenViewModel.clearErrorMessage()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("link_shortener_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Link Minify",
                        modifier = Modifier.testTag("app_title")
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("links_list"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    InputLink(
                        isLoading = uiState.isLoading,
                        onShortenClick = {
                            focusManager.clearFocus()
                            mainScreenViewModel.shortenLink(it)
                        },
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (uiState.listShortLinks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recent Links",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("recent_links_header")
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                if (uiState.listShortLinks.isEmpty() && !uiState.isLoading) {
                    item {
                        EmptyState(
                            title = "No shortened links yet",
                            subtitle = "Enter a URL above to create your first short link",
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(16.dp)
                        )
                    }
                } else {
                    items(
                        items = uiState.listShortLinks,
                        key = { it.id }
                    ) { minifyLink ->
                        ShortenedLinkItem(
                            originalUrl = minifyLink.url,
                            shortUrl = minifyLink.shortUrl,
                            formattedTimestamp = minifyLink.timestamp.getFormatTimestamp(),
                            linkId = minifyLink.alias,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            LoadingOverlay(
                isVisible = uiState.isLoading,
                loadingMessage = "Loading, please wait",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}