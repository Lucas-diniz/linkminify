package io.link.minify.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.link.minify.ui.theme.LinkMinifyTheme

/**
 * LoadingOverlay Component
 *
 * A reusable composable that displays a loading indicator overlay following the Single Responsibility Principle.
 * This component is responsible solely for displaying a loading state with proper accessibility support.
 *
 * SOLID Principles Applied:
 * - Single Responsibility: Handles only the display of loading state
 * - Open/Closed: Extensible through visibility parameter, closed for modification
 * - Liskov Substitution: Can be substituted anywhere a loading overlay is needed
 * - Interface Segregation: Accepts only the parameters it needs (visibility and description)
 * - Dependency Inversion: No dependencies on concrete implementations
 *
 * @param isVisible Whether the loading overlay should be visible
 * @param loadingMessage The accessibility message for screen readers
 * @param modifier Optional modifier for customization
 */
@Composable
fun LoadingOverlay(
    isVisible: Boolean,
    loadingMessage: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .size(100.dp)
                .testTag("loading_overlay"),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            shadowElevation = 8.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription = loadingMessage
                        }
                )
            }
        }
    }
}

// Preview Functions

@Preview(
    name = "Loading Overlay - Visible",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun LoadingOverlayVisiblePreview() {
    LinkMinifyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingOverlay(
                isVisible = true,
                loadingMessage = "Loading, please wait",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(
    name = "Loading Overlay - Hidden",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun LoadingOverlayHiddenPreview() {
    LinkMinifyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingOverlay(
                isVisible = false,
                loadingMessage = "Loading, please wait",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(
    name = "Loading Overlay - Custom Message",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun LoadingOverlayCustomMessagePreview() {
    LinkMinifyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingOverlay(
                isVisible = true,
                loadingMessage = "Shortening your URL, please wait",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}