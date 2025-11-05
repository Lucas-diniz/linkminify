package io.link.minify.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.link.minify.core.extensions.isValidUrl
import io.link.minify.ui.theme.LinkMinifyTheme
import io.link.minify.ui.toMessageRes

/**
 * InputLink Component
 *
 * A stateful composable that provides URL input functionality with built-in validation,
 * following Clean Architecture and SOLID principles. This component manages its own input
 * state internally and validates URLs in real-time using the [isValidUrl] extension function.
 *
 * ## Features
 * - Real-time URL validation with visual feedback (error icon and supporting text)
 * - Disabled state during loading operations to prevent duplicate submissions
 * - URI-optimized keyboard type for better user experience
 * - Accessibility support with content descriptions and semantic properties
 * - Test tags for UI testing integration
 *
 * ## Usage Example
 * ```kotlin
 * InputLink(
 *     onShortenClick = { url ->
 *         viewModel.shortenUrl(url)
 *     },
 *     isLoading = uiState.isLoading
 * )
 * ```
 *
 * @param onShortenClick Callback invoked when the user clicks the "Shorten Link" button.
 *                       Receives the validated URL string as a parameter. This callback is
 *                       only triggered when the URL is valid and loading is false.
 * @param isLoading Boolean flag indicating whether a URL shortening operation is in progress.
 *                  When true, both the input field and button are disabled to prevent
 *                  duplicate submissions and the button displays "Shortening..." text.
 * @param modifier Optional [Modifier] for customizing the layout and appearance of the component.
 *                 Applied to the root Column container. Defaults to [Modifier] (no modifications).
 *
 * @see [isValidUrl] for URL validation logic
 */
@Composable
fun InputLink(
    onShortenClick: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var urlInput by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("input_section"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("url_input_field")
                .semantics {
                    contentDescription = "Enter the URL you want to shorten"
                },
            label = { Text("Enter URL to shorten") },
            placeholder = { Text("https://example.com/long-url") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Link icon"
                )
            },
            trailingIcon = {
                if (urlInput.isNotBlank() && !urlInput.isValidUrl().first) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = urlInput.isValidUrl().second.toString(),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            isError = !urlInput.isValidUrl().first,
            supportingText = {
                urlInput.isValidUrl().second?.let {
                    Text(
                        text = stringResource(it.toMessageRes()),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.testTag("url_error_text")
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onShortenClick(urlInput) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("shorten_button")
                .semantics {
                    contentDescription = "Shorten the URL button"
                },
            enabled = urlInput.isValidUrl().first && !isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isLoading) "Shortening..." else "Shorten Link",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * Stateless version of InputLink for preview purposes.
 * This allows us to demonstrate different states without managing internal state.
 * This component is private and only used for previews.
 */
@Composable
private fun InputLinkStateless(
    urlInput: String,
    onUrlChange: (String) -> Unit,
    onShortenClick: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("input_section"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = urlInput,
            onValueChange = onUrlChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("url_input_field")
                .semantics {
                    contentDescription = "Enter the URL you want to shorten"
                },
            label = { Text("Enter URL to shorten") },
            placeholder = { Text("https://example.com/long-url") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Link icon"
                )
            },
            trailingIcon = {
                if (urlInput.isNotBlank() && !urlInput.isValidUrl().first) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = urlInput.isValidUrl().second.toString(),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            isError = urlInput.isNotBlank() && !urlInput.isValidUrl().first,
            supportingText = {
                if (urlInput.isNotBlank() && !urlInput.isValidUrl().first) {
                    Text(
                        text = "Please enter a valid URL starting with http:// or https://",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.testTag("url_error_text")
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onShortenClick(urlInput) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("shorten_button")
                .semantics {
                    contentDescription = "Shorten the URL button"
                },
            enabled = urlInput.isValidUrl().first && !isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isLoading) "Shortening..." else "Shorten Link",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// =================================================================================================
// Preview Functions - Light Theme States
// =================================================================================================

/**
 * Preview showing the initial empty state of the input section.
 * Demonstrates the component when first loaded with no user interaction.
 * - Empty text field with placeholder
 * - Button is disabled (no URL entered)
 * - Light theme
 */
@Preview(
    name = "Empty State",
    group = "Input Section - Light Theme",
    showBackground = true
)
@Composable
private fun InputSectionEmptyPreview() {
    LinkMinifyTheme(darkTheme = false) {
        InputLinkStateless(
            urlInput = "",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

/**
 * Preview showing the component with a valid URL entered.
 * Demonstrates successful validation state.
 * - Valid URL: https://example.com/very/long/url/path
 * - Button is enabled and ready to submit
 * - No error indicators visible
 * - Light theme
 */
@Preview(
    name = "Valid URL - Ready",
    group = "Input Section - Light Theme",
    showBackground = true
)
@Composable
private fun InputSectionValidUrlPreview() {
    LinkMinifyTheme(darkTheme = false) {
        InputLinkStateless(
            urlInput = "https://example.com/very/long/url/path",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

/**
 * Preview showing the component with an invalid URL entered.
 * Demonstrates error validation state with visual feedback.
 * - Invalid URL: not-a-valid-url
 * - Warning icon displayed in trailing position
 * - Error text shown below input field
 * - Button is disabled (cannot submit invalid URL)
 * - Light theme
 */
@Preview(
    name = "Invalid URL - Error",
    group = "Input Section - Light Theme",
    showBackground = true
)
@Composable
private fun InputSectionInvalidUrlPreview() {
    LinkMinifyTheme(darkTheme = false) {
        InputLinkStateless(
            urlInput = "not-a-valid-url",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

/**
 * Preview showing the component during URL shortening operation.
 * Demonstrates the loading state behavior and disabled interactions.
 * - Valid URL entered
 * - TextField is disabled (prevents editing during operation)
 * - Button shows "Shortening..." text
 * - Button is disabled (prevents duplicate submissions)
 * - Light theme
 */
@Preview(
    name = "Loading State",
    group = "Input Section - Light Theme",
    showBackground = true
)
@Composable
private fun InputSectionLoadingPreview() {
    LinkMinifyTheme(darkTheme = false) {
        InputLinkStateless(
            urlInput = "https://example.com/long-url",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = true
        )
    }
}

/**
 * Preview showing partial URL entry with validation error.
 * Demonstrates real-time validation during user input.
 * - Incomplete URL: http:// (no domain)
 * - Warning icon displayed
 * - Error message shown (invalid format)
 * - Button is disabled
 * - Light theme
 */
@Preview(
    name = "Partial URL - Error",
    group = "Input Section - Light Theme",
    showBackground = true
)
@Composable
private fun InputSectionPartialUrlPreview() {
    LinkMinifyTheme(darkTheme = false) {
        InputLinkStateless(
            urlInput = "http://",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

/**
 * Preview showing the component with a long, complex valid URL.
 * Demonstrates text overflow handling and layout with lengthy URLs.
 * - Very long URL with multiple path segments and query parameters
 * - Button is enabled (valid URL)
 * - Single-line text field behavior (horizontal scrolling)
 * - Light theme
 */
@Preview(
    name = "Long Valid URL",
    group = "Input Section - Light Theme",
    showBackground = true
)
@Composable
private fun InputSectionLongUrlPreview() {
    LinkMinifyTheme(darkTheme = false) {
        InputLinkStateless(
            urlInput = "https://www.example.com/very/long/path/with/multiple/segments?param1=value1&param2=value2&param3=value3",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

// =================================================================================================
// Preview Functions - Dark Theme States
// =================================================================================================

/**
 * Preview showing empty state in dark theme.
 * Verifies proper contrast, colors, and readability in dark mode.
 * - Empty input field
 * - Disabled button
 * - Dark theme colors applied
 */
@Preview(
    name = "Empty State - Dark",
    group = "Input Section - Dark Theme",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun InputSectionEmptyDarkPreview() {
    LinkMinifyTheme(darkTheme = true) {
        InputLinkStateless(
            urlInput = "",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

/**
 * Preview showing valid URL state in dark theme.
 * Verifies button and text field appearance with proper contrast in dark mode.
 * - Valid URL entered
 * - Enabled button state
 * - Dark theme colors applied
 */
@Preview(
    name = "Valid URL - Dark",
    group = "Input Section - Dark Theme",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun InputSectionValidUrlDarkPreview() {
    LinkMinifyTheme(darkTheme = true) {
        InputLinkStateless(
            urlInput = "https://example.com/very/long/url/path",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

/**
 * Preview showing error state in dark theme.
 * Verifies error colors have sufficient contrast and are accessible in dark mode.
 * - Invalid URL with error state
 * - Warning icon visible
 * - Error text with proper dark theme error color
 * - Disabled button
 */
@Preview(
    name = "Invalid URL - Dark",
    group = "Input Section - Dark Theme",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun InputSectionInvalidUrlDarkPreview() {
    LinkMinifyTheme(darkTheme = true) {
        InputLinkStateless(
            urlInput = "not-a-valid-url",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = false
        )
    }
}

/**
 * Preview showing loading state in dark theme.
 * Verifies disabled state appearance and button text in dark mode.
 * - Loading state active
 * - Disabled input field
 * - Button shows "Shortening..." text
 * - Dark theme colors applied
 */
@Preview(
    name = "Loading - Dark",
    group = "Input Section - Dark Theme",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun InputSectionLoadingDarkPreview() {
    LinkMinifyTheme(darkTheme = true) {
        InputLinkStateless(
            urlInput = "https://example.com/long-url",
            onUrlChange = {},
            onShortenClick = {},
            isLoading = true
        )
    }
}