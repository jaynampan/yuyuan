package meow.softer.yuyuan.ui.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.EntryPointAccessors

/**
 * The main UI of a learning session
 */
@Composable
fun PlaygroundScreen(
    playgroundViewModel: PlaygroundViewModel,
    onBack: () -> Unit
) {
    PlaygroundContent {
        WordMemoryScreen(
            playgroundViewModel,
            onBack = onBack
        )
    }
}

@Composable
fun PlaygroundContent(content: @Composable () -> Unit) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            Color.White.copy(alpha = 0.8f)
        ) // Start and end colors
    )
    Box(
        modifier = Modifier
            .background(gradientBrush)
            .fillMaxSize()
    ) {
        content()
    }
}


@Preview
@Composable
fun PlaygroundPreview() {
    val playgroundViewModel :PlaygroundViewModel = EntryPointAccessors.fromApplication(
        LocalContext.current,PlaygroundViewModel::class.java
    )
    PlaygroundScreen(
        playgroundViewModel = playgroundViewModel,
        onBack = {}
    )
}
