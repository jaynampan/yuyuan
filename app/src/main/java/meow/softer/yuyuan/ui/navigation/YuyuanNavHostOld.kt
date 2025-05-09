package meow.softer.yuyuan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import meow.softer.yuyuan.ui.home.HomeScreen
import meow.softer.yuyuan.ui.home.MainViewModel
import meow.softer.yuyuan.ui.playground.PlaygroundScreen
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.ui.playground.WriteScreen
import meow.softer.yuyuan.ui.setting.SettingScreen


@Composable
fun YuyuanNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    playgroundViewModel: PlaygroundViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                mainViewModel = mainViewModel,
                onRouteClick = { route ->
                    navController.navigateSingleTop(route)
                }
            )
        }

        composable(route = Playground.route) {
            PlaygroundScreen(
                viewModel = playgroundViewModel,
                onBack = {
                    navController.navigateSingleTop(Home.route)
                    playgroundViewModel.stopAudio()
                },
                navigateTo = { navController.navigate(it) }
            )
        }

        composable(route = Setting.route) {
            SettingScreen(
                onBack = {
                    navController.navigateSingleTop(Home.route)
                },
                mainViewModel = mainViewModel
            )
        }
        composable(route = Write.route) {
            WriteScreen(
                viewModel = playgroundViewModel,
                onBack = {navController.popBackStack() }
            )
        }

    }
}


fun NavHostController.navigateSingleTop(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTop.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}