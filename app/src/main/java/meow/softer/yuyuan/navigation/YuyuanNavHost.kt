package meow.softer.yuyuan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import meow.softer.yuyuan.ui.calendar.CalendarScreen
import meow.softer.yuyuan.ui.home.HomeScreen
import meow.softer.yuyuan.ui.home.HomeViewModel
import meow.softer.yuyuan.ui.home.LessonScreen
import meow.softer.yuyuan.ui.home.MeScreen
import meow.softer.yuyuan.ui.playground.PlaygroundScreen
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.ui.profile.ProfileScreen
import meow.softer.yuyuan.ui.setting.SettingScreen

@Composable
fun YuyuanNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    playgroundViewModel: PlaygroundViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onButtonClick = {
                    navController.navigateSingleTop(Playground.route)
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
        composable(route = Setting.route) {
            SettingScreen()
        }
        composable(route = Calendar.route) {
            CalendarScreen()
        }
        // playground
        composable(route = Playground.route) {
            PlaygroundScreen(
                playgroundViewModel = playgroundViewModel,
                onBack = { navController.popBackStack() }
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