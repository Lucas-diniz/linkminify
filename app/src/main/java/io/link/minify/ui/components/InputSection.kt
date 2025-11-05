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
import io.link.minify.isValidUrl
import io.link.minify.ui.theme.LinkMinifyTheme
import io.link.minify.ui.toMessageRes

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

// Preview Functions
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