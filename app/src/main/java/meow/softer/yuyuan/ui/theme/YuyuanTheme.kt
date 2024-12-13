package meow.softer.yuyuan.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


private val GreenColorScheme = lightColorScheme(
    primary = Color(0xFF1EB980),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    // 添加其他颜色配置
)
private val BlueColorScheme = lightColorScheme(
    primary = Color(0xFF1EB980),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    // 添加其他颜色配置
)


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1EB980),
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.White,
    // 添加其他颜色配置
)

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit) = this.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick
)


@Composable
fun YuyuanTheme(
    mode: ThemeMode = ThemeMode.Blue,
    content: @Composable () -> Unit
) {
    val colorScheme = when (mode) {
        ThemeMode.Blue -> BlueColorScheme
        ThemeMode.Dark -> DarkColorScheme
        ThemeMode.Green -> GreenColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}