package io.link.minify.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.link.minify.R
import io.link.minify.ui.theme.LinkMinifyTheme

@Composable
fun ShortenedLinkItem(
    originalUrl: String,
    shortUrl: String,
    formattedTimestamp: String,
    linkId: String,
    modifier: Modifier = Modifier,
) {
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
    Card(
        modifier =
            modifier
                .testTag("link_item_$linkId"),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        onClick = { uriHandler.openUri(originalUrl) },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formattedTimestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("link_timestamp_$linkId"),
                )
            }

            Text(
                text = stringResource(R.string.label_original),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = originalUrl,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("original_url_$linkId"),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.label_shortened),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = shortUrl,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("short_url_$linkId"),
                    )
                }
            }
        }
    }
}

// Preview Functions
@Preview(
    name = "Link Item - Standard",
    showBackground = true,
)
@Composable
private fun ShortenedLinkItemPreview() {
    LinkMinifyTheme {
        ShortenedLinkItem(
            originalUrl = "https://developer.android.com/jetpack/compose/documentation",
            shortUrl = "https://lnk.mn/aB3dEf",
            formattedTimestamp = "Nov 03, 2025 at 14:30",
            linkId = "1",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        )
    }
}

@Preview(
    name = "Link Item - Long URL",
    showBackground = true,
)
@Composable
private fun ShortenedLinkItemLongUrlPreview() {
    LinkMinifyTheme {
        ShortenedLinkItem(
            originalUrl = "https://very-long-domain-name-example.com/with/multiple/path/segments/and/query/parameters?foo=bar&baz=qux",
            shortUrl = "https://lnk.mn/xY9zAb",
            formattedTimestamp = "Nov 02, 2025 at 09:15",
            linkId = "2",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        )
    }
}

@Preview(
    name = "Link Item - Zero Clicks",
    showBackground = true,
)
@Composable
private fun ShortenedLinkItemZeroClicksPreview() {
    LinkMinifyTheme {
        ShortenedLinkItem(
            originalUrl = "https://github.com/android/architecture-samples",
            shortUrl = "https://lnk.mn/cD5eF7",
            formattedTimestamp = "Nov 03, 2025 at 16:00",
            linkId = "3",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        )
    }
}
