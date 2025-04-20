package meow.softer.yuyuan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import meow.softer.yuyuan.ui.features.CalendarScreen
import meow.softer.yuyuan.ui.features.InboxScreen
import meow.softer.yuyuan.ui.home.HomeScreenOld
import meow.softer.yuyuan.ui.home.HomeViewModelOld
import meow.softer.yuyuan.ui.home.LessonScreen
import meow.softer.yuyuan.ui.home.MeScreen
import meow.softer.yuyuan.ui.playground.PlaygroundScreenOld
import meow.softer.yuyuan.ui.playground.PlaygroundViewModelOld
import meow.softer.yuyuan.ui.profile.ProfileScreen
import meow.softer.yuyuan.ui.features.SearchScreen
import meow.softer.yuyuan.ui.home.HomeScreen
import meow.softer.yuyuan.ui.home.MainViewModel
import meow.softer.yuyuan.ui.playground.PlaygroundScreen
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.ui.setting.SettingScreen

@Composable
fun YuyuanNavHostOld(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeViewModelOld: HomeViewModelOld,
    playgroundViewModelOld: PlaygroundViewModelOld
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreenOld(
                homeViewModelOld = homeViewModelOld,
                onRouteClick = { route ->
                    navController.navigateSingleTop(route)
                }
            )
        }
        composable(route = Lesson.route) {
            LessonScreen()
        }
        composable(route = Me.route) {
            MeScreen()
        }
        // feature screens
        composable(route = Profile.route) {
            ProfileScreen()
        }

        composable(route = Calendar.route) {
            CalendarScreen()
        }
        // playground
        composable(route = Playground.route) {
            PlaygroundScreenOld(
                viewModel = playgroundViewModelOld,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Search.route) {
            SearchScreen()
        }
        composable(route = Inbox.route) {
            InboxScreen()
        }
    }
}

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
                }
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