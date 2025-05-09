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
object Write: YuyuanDestination {
    override val route: String = "Write"
}


// Screens to be displayed in the top YuyuanTabRow
val yuyuanTabRowScreens = listOf(Home, Lesson, Me)

