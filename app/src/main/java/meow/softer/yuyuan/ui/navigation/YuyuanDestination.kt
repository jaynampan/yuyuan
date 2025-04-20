package meow.softer.yuyuan.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Contract for information needed on every Yuyuan navigation destination
 */
interface YuyuanDestination {
    val route: String
}

/**
 * Screens in Home
 */
interface HomeDestination : YuyuanDestination {
    val icon: ImageVector
}

/**
 * Screens for playground
 */
interface PlaygroundDestination : YuyuanDestination

/**
 * Yuyuan app home navigation destinations
 */
object Home : HomeDestination {
    override val icon = Icons.Filled.Home
    override val route = "Home"
}

object Lesson : HomeDestination {
    override val icon = Icons.Filled.Face
    override val route = "Lesson"
}

object Me : HomeDestination {
    override val icon = Icons.Filled.Person
    override val route = "Me"
}

/**
 * Playground destinations
 */

object WordMemory : PlaygroundDestination {
    override val route: String = "WordMemory"
}

object WordDetail : PlaygroundDestination {
    override val route: String = "WordDetail"
}

object WordWriting : PlaygroundDestination {
    override val route: String = "WordWriting"
}

/**
 * Yuyuan app other navigation destinations
 */

object Profile : YuyuanDestination {
    override val route: String = "Profile"
}

object Setting : YuyuanDestination {
    override val route: String = "Setting"
}

object Calendar : YuyuanDestination {
    override val route: String = "Calendar"
}

object Playground : YuyuanDestination {
    override val route: String = "Playground"
}

object Inbox : YuyuanDestination {
    override val route: String = "Inbox"
}
object Search : YuyuanDestination {
    override val route: String = "Search"
}

// Screens to be displayed in the top YuyuanTabRow
val yuyuanTabRowScreens = listOf(Home, Lesson, Me)

