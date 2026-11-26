package ca.itwebsolutions.hindmarket.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ca.itwebsolutions.hindmarket.model.FontScalePreset

val SaffronPrimary = Color(0xFFD84315)
val EmeraldSecondary = Color(0xFF1B5E20)
val WarmBackground = Color(0xFFF7F5F0)
val CardSurface = Color(0xFFFFFFFF)
val DarkInk = Color(0xFF1A1A1A)

@Composable
fun HindMarketTheme(
    fontScale: FontScalePreset,
    content: @Composable () -> Unit
) {
    val m = fontScale.multiplier
    val typography = Typography(
        headlineMedium = TextStyle(fontSize = (24 * m).sp, fontWeight = FontWeight.Bold, color = DarkInk),
        titleLarge = TextStyle(fontSize = (20 * m).sp, fontWeight = FontWeight.Bold, color = DarkInk),
        titleMedium = TextStyle(fontSize = (18 * m).sp, fontWeight = FontWeight.SemiBold, color = DarkInk),
        bodyLarge = TextStyle(fontSize = (17 * m).sp, lineHeight = (24 * m).sp, color = DarkInk),
        bodyMedium = TextStyle(fontSize = (15 * m).sp, lineHeight = (21 * m).sp, color = Color(0xFF333333)),
        labelLarge = TextStyle(fontSize = (17 * m).sp, fontWeight = FontWeight.Bold)
    )

    val colors = lightColorScheme(
        primary = SaffronPrimary,
        secondary = EmeraldSecondary,
        background = WarmBackground,
        surface = CardSurface,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = DarkInk,
        onSurface = DarkInk
    )

    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
