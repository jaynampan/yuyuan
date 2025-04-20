package meow.softer.yuyuan.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import meow.softer.yuyuan.ui.navigation.HomeDestination

/**
 * Yuyuan's bottom navigation tab row
 */
@Composable
fun YuyuanNavBar(
    homeScreens: List<HomeDestination>,
    onTabSelected: (HomeDestination) -> Unit,
    currentScreen: HomeDestination
) {
    NavigationBar (
        modifier = Modifier.fillMaxWidth()
    ){
        homeScreens.forEach { screen->
            NavigationBarItem(
                selected = screen.route == currentScreen.route,
                onClick = {onTabSelected(screen)},
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                }
            )

        }
    }
}