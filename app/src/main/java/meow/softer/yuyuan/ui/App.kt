package meow.softer.yuyuan.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import meow.softer.yuyuan.ui.home.MainViewModel
import meow.softer.yuyuan.ui.navigation.YuyuanNavHost
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel

@Composable
fun App(
    mainViewModel: MainViewModel,
    playgroundViewModel: PlaygroundViewModel
) {
    val navController = rememberNavController()
    YuyuanNavHost(
        navController = navController,
        modifier = Modifier,
        mainViewModel = mainViewModel,
        playgroundViewModel = playgroundViewModel
    )
}