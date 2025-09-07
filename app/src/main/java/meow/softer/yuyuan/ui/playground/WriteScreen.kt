package meow.softer.yuyuan.ui.playground

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WriteScreen(
    onBack: () -> Unit,
    viewModel: PlaygroundViewModel
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val hanziInfo = when (uiState) {
        is PlaygroundUiState.NoData -> null
        is PlaygroundUiState.HasData -> uiState.hanziInfo
    }
    val onFinish = {
        onBack()
        viewModel.onNextClick()
    }
    //todo: loading
    if (hanziInfo != null) {
        var replay by remember { mutableIntStateOf(0) }
        val onReplay = { replay++ }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadBarV1(
                onBack = {
                    onBack()
                }
            )
            Spacer(
                modifier =
                    Modifier.height(50.dp)
            )

            WebPage(
                hanziInfo.word.character,
                onTap = {
                    //todo
                },
                replay = replay,
                showMode = false
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Text(
                modifier = Modifier
                    .clickable { onReplay()},
                text = "Write Again",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )

            ButtonArea(onNextClick = { onFinish() })
        }

    } else {
        Text("Loading...")
    }

}
//TODO: convert to native as js is not good enough at performance
// Known issues: Start up at playground for first time would have white screen for a sec
// as the webpage is loading
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPage(
    char: String,
    modifier: Modifier = Modifier,
    showMode: Boolean = true,
    replay:Int = 1,
    onTap: () -> Unit ={},
) {

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true // Enable JavaScript in the WebView
                webChromeClient = WebChromeClient() // Handle JavaScript dialogs, progress, etc.
                loadUrl("file:///android_asset/animate.html") // Load a URL
                val jsonFile = "char_json/$char.json"
                val jsonContent =
                    context.assets.open(jsonFile).bufferedReader().use { it.readText() }
                loadUrl("file:///android_asset/animate.html")
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        val jsCode = "javascript: animate(\"$char\",$jsonContent,$showMode);"
                        evaluateJavascript(jsCode, null)
                    }

                }
            }
        },
        modifier = modifier.pointerInput(Unit) {
            onTap()
        },
        update = { webview ->
            if(replay > 0){
                val jsonFile = "char_json/$char.json"
                val jsonContent =
                    webview.context.assets.open(jsonFile).bufferedReader().use { it.readText() }
                val jsCode = "javascript: animate(\"$char\",$jsonContent,$showMode);"
                webview.evaluateJavascript(jsCode, null)
            }
        }
    )
}
