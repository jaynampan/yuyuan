package meow.softer.yuyuan.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import meow.softer.yuyuan.ui.home.HomeViewModel
import meow.softer.yuyuan.ui.navigation.YuyuanNavHost
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.ui.setting.SettingViewModel

@Composable
fun App(
    homeViewModel: HomeViewModel,
    playgroundViewModel: PlaygroundViewModel,
    settingViewModel: SettingViewModel
) {
    val navController = rememberNavController()
    YuyuanNavHost(
        navController = navController,
        modifier = Modifier,
        homeViewModel = homeViewModel,
        playgroundViewModel = playgroundViewModel,
        settingViewModel = settingViewModel
    )
}