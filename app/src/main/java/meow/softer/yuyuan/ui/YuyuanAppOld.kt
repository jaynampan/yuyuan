package meow.softer.yuyuan.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import meow.softer.yuyuan.ui.components.YuyuanNavBar
import meow.softer.yuyuan.ui.home.MainViewModel
import meow.softer.yuyuan.ui.navigation.Home
import meow.softer.yuyuan.ui.navigation.HomeDestination
import meow.softer.yuyuan.ui.navigation.YuyuanNavHost
import meow.softer.yuyuan.ui.navigation.navigateSingleTop
import meow.softer.yuyuan.ui.navigation.yuyuanTabRowScreens
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel

@Composable
fun YuyuanApp(
    mainViewModel: MainViewModel,
    playgroundViewModel: PlaygroundViewModel
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentHomeScreen: HomeDestination =
        yuyuanTabRowScreens.find { it.route == currentDestination?.route } ?: Home
    Scaffold(
        bottomBar = {
            if (currentDestination?.route in yuyuanTabRowScreens.map { it.route }) {
                YuyuanNavBar(
                    homeScreens = yuyuanTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTop(newScreen.route)
                    },
                    currentScreen = currentHomeScreen
                )
            }

        }
    ) { innerPaddings ->
        val navController = rememberNavController()
        YuyuanNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings),
            mainViewModel = mainViewModel,
            playgroundViewModel = playgroundViewModel
        )
    }
}
