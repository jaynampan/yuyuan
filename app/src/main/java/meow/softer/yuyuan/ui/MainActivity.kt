package meow.softer.yuyuan.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import meow.softer.yuyuan.ui.home.HomeViewModel
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.ui.theme.YuyuanTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val homeViewModel: HomeViewModel by viewModels()
        val playgroundViewModel: PlaygroundViewModel by viewModels()
        setContent {
            YuyuanTheme {
                YuyuanApp(
                    homeViewModel = homeViewModel,
                    playgroundViewModel = playgroundViewModel
                )
            }
        }
    }

}