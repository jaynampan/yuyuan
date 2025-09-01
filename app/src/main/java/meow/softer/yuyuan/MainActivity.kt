package meow.softer.yuyuan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import meow.softer.yuyuan.ui.App
import meow.softer.yuyuan.ui.home.MainViewModel
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.ui.theme.YuyuanTheme
import meow.softer.yuyuan.utils.debug


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: MainViewModel by viewModels()
    private val playgroundViewModel: PlaygroundViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            YuyuanTheme {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.Companion.fillMaxSize()
                ) {
                    App(
                        mainViewModel = homeViewModel,
                        playgroundViewModel = playgroundViewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        debug("Main activity onStart this is called once!")
        homeViewModel.initSharedViewModel(this)
        playgroundViewModel.initSharedViewModel(this)
    }
}