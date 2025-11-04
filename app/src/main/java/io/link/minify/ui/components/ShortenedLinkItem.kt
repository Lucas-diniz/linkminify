package io.link.minify.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.link.minify.ui.theme.LinkMinifyTheme

/**
 * ShortenedLinkItem Component
 *
 * A reusable composable that displays a single shortened link card following the Single Responsibility Principle.
 * This component is responsible solely for displaying link information and providing a copy action.
 *
 * SOLID Principles Applied:
 * - Single Responsibility: Handles only the display of a single link item
 * - Open/Closed: Extensible through callbacks and data parameters, closed for modification
 * - Liskov Substitution: Can be substituted anywhere a link display is needed
 * - Interface Segregation: Accepts only the data and callbacks it needs
 * - Dependency Inversion: Depends on data structure abstraction and function types
 *
 * @param originalUrl The original long URL
 * @param shortUrl The shortened URL
 * @param formattedTimestamp The formatted timestamp string
 * @param clickCount The number of clicks on the shortened link
 * @param linkId Unique identifier for the link (used for test tags)
 * @param onCopyClick Callback invoked when the copy button is clicked
 * @param modifier Optional modifier for customization
 */
@Composable
fun ShortenedLinkItem(
    originalUrl: String,
    shortUrl: String,
    formattedTimestamp: String,
    clickCount: Int,
    linkId: String,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("link_item_$linkId"),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with timestamp and click count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedTimestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("link_timestamp_$linkId")
                )

                Text(
                    text = "$clickCount clicks",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("link_clicks_$linkId")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Original URL section
            Text(
                text = "Original:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = originalUrl,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("original_url_$linkId")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Shortened URL section with copy button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Shortened:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = shortUrl,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("short_url_$linkId")
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onCopyClick,
                    modifier = Modifier
                        .testTag("copy_button_$linkId")
                        .semantics {
                            contentDescription = "Copy shortened URL to clipboard"
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Copy",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// Preview Functions

@Preview(
    name = "Link Item - Standard",
    showBackground = true
)
@Composable
private fun ShortenedLinkItemPreview() {
    LinkMinifyTheme {
        ShortenedLinkItem(
            originalUrl = "https://developer.android.com/jetpack/compose/documentation",
            shortUrl = "https://lnk.mn/aB3dEf",
            formattedTimestamp = "Nov 03, 2025 at 14:30",
            clickCount = 42,
            linkId = "1",
            onCopyClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(
    name = "Link Item - Long URL",
    showBackground = true
)
@Composable
private fun ShortenedLinkItemLongUrlPreview() {
    LinkMinifyTheme {
        ShortenedLinkItem(
            originalUrl = "https://very-long-domain-name-example.com/with/multiple/path/segments/and/query/parameters?foo=bar&baz=qux",
            shortUrl = "https://lnk.mn/xY9zAb",
            formattedTimestamp = "Nov 02, 2025 at 09:15",
            clickCount = 156,
            linkId = "2",
            onCopyClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(
    name = "Link Item - Zero Clicks",
    showBackground = true
)
@Composable
private fun ShortenedLinkItemZeroClicksPreview() {
    LinkMinifyTheme {
        ShortenedLinkItem(
            originalUrl = "https://github.com/android/architecture-samples",
            shortUrl = "https://lnk.mn/cD5eF7",
            formattedTimestamp = "Nov 03, 2025 at 16:00",
            clickCount = 0,
            linkId = "3",
            onCopyClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}