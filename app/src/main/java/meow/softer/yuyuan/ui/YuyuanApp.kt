package meow.softer.yuyuan.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import meow.softer.yuyuan.navigation.Home
import meow.softer.yuyuan.navigation.HomeDestination
import meow.softer.yuyuan.navigation.YuyuanNavHost
import meow.softer.yuyuan.navigation.navigateSingleTop
import meow.softer.yuyuan.navigation.yuyuanTabRowScreens
import meow.softer.yuyuan.ui.components.YuyuanNavBar
import meow.softer.yuyuan.ui.home.HomeViewModel
import meow.softer.yuyuan.ui.playground.PlaygroundViewModel
import meow.softer.yuyuan.ui.theme.YuyuanTheme

@Composable
fun YuyuanApp(
    homeViewModel: HomeViewModel,
    playgroundViewModel: PlaygroundViewModel
) {
    YuyuanTheme {
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
            YuyuanNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPaddings),
                homeViewModel = homeViewModel,
                playgroundViewModel = playgroundViewModel
            )
        }
    }
}