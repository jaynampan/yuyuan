package meow.softer.yuyuan.ui.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import meow.softer.yuyuan.ui.navigation.Write
import meow.softer.yuyuan.utils.debug

/**
 * The main UI of a learning session
 */
@Composable
fun PlaygroundScreen(
    viewModel: PlaygroundViewModel,
    onBack: () -> Unit,
    navigateTo: (String) -> Unit
) {
    // Collect the navigateBack state
    val navigateBack by viewModel.navigateBack.collectAsState()
    // When the navigateBack state changes to true, trigger navigation
    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            onBack() // Navigate back
        }
    }
    // Stop and release media player when this screen is disposed
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                debug("ON_STOP")
                viewModel.stopAudio()
            }

            else -> Unit
        }
    }

    DisposableEffect(Unit) {
        lifecycle.addObserver(observer)
        debug("added listener")
        // remove the observer when screen is removed
        onDispose {
            debug("removed lifecycle listener ")
            lifecycle.removeObserver(observer)
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val hanziInfo = when (uiState) {
        is PlaygroundUiState.NoData -> null
        is PlaygroundUiState.HasData -> uiState.hanziInfo
    }
    //todo: loading

    if (hanziInfo != null) {
        PlaygroundContent {
            WordMemoryScreen(
                currentHanzi = hanziInfo,
                onBack = onBack,
                playWordSound = { viewModel.playWordSound() },
                playSentenceSound = { viewModel.playSentenceSound() },
                onNext = { viewModel.onNextClick() },
                onWrite = { navigateTo(Write.route) }
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            Text("Loading.....")
        }
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


