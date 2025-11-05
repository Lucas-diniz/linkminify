package io.link.minify.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.link.minify.ui.theme.LinkMinifyTheme

/**
 * EmptyState Component
 *
 * A reusable composable that displays an empty state UI following the Single Responsibility Principle.
 * This component is responsible solely for displaying a message when there are no items to show.
 *
 * SOLID Principles Applied:
 * - Single Responsibility: Handles only the display of empty state
 * - Open/Closed: Extensible through parameters, closed for modification
 * - Liskov Substitution: Can be substituted anywhere an empty state display is needed
 * - Interface Segregation: Accepts only the parameters it needs
 * - Dependency Inversion: Depends on abstraction (display text), not concrete implementations
 *
 * @param title The main title text to display
 * @param subtitle The subtitle or description text to display
 * @param modifier Optional modifier for customization
 */
@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.testTag("empty_state"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("empty_state_title")
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("empty_state_subtitle")
        )
    }
}

// Preview Functions

@Preview(
    name = "Empty State - Default",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun EmptyStatePreview() {
    LinkMinifyTheme {
        EmptyState(
            title = "No shortened links yet",
            subtitle = "Enter a URL above to create your first short link"
        )
    }
}

@Preview(
    name = "Empty State - Custom Message",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun EmptyStateCustomPreview() {
    LinkMinifyTheme {
        EmptyState(
            title = "No results found",
            subtitle = "Try adjusting your filters or create a new link"
        )
    }
}

@Preview(
    name = "Empty State - Long Text",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun EmptyStateLongTextPreview() {
    LinkMinifyTheme {
        EmptyState(
            title = "Your link history is empty",
            subtitle = "Start shortening URLs to see your history here. All your shortened links will be saved and displayed in this section."
        )
    }
}