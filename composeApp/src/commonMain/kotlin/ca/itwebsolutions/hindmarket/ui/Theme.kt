package ca.itwebsolutions.hindmarket.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ca.itwebsolutions.hindmarket.model.FontScalePreset

// Minimal Modern Palette
val TerracottaPrimary = Color(0xFFC2410C)
val TerracottaSoft = Color(0xFFFFF7ED)
val ForestVerified = Color(0xFF065F46)
val ForestSoft = Color(0xFFECFDF5)
val CanvasBackground = Color(0xFFF8F9FA)
val SurfaceWhite = Color(0xFFFFFFFF)
val HairlineBorder = Color(0xFFE5E7EB)
val InkPrimary = Color(0xFF111827)
val InkSecondary = Color(0xFF4B5563)

@Composable
fun HindMarketTheme(
    fontScale: FontScalePreset,
    content: @Composable () -> Unit
) {
    val m = fontScale.multiplier
    val typography = Typography(
        headlineMedium = TextStyle(
            fontSize = (24 * m).sp,
            lineHeight = (32 * m).sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp,
            color = InkPrimary
        ),
        titleLarge = TextStyle(
            fontSize = (19 * m).sp,
            lineHeight = (26 * m).sp,
            fontWeight = FontWeight.Bold,
            color = InkPrimary
        ),
        titleMedium = TextStyle(
            fontSize = (17 * m).sp,
            lineHeight = (24 * m).sp,
            fontWeight = FontWeight.SemiBold,
            color = InkPrimary
        ),
        bodyLarge = TextStyle(
            fontSize = (16 * m).sp,
            lineHeight = (24 * m).sp,
            color = InkPrimary
        ),
        bodyMedium = TextStyle(
            fontSize = (14 * m).sp,
            lineHeight = (20 * m).sp,
            color = InkSecondary
        ),
        labelLarge = TextStyle(
            fontSize = (16 * m).sp,
            fontWeight = FontWeight.SemiBold
        )
    )

    val colors = lightColorScheme(
        primary = TerracottaPrimary,
        secondary = ForestVerified,
        background = CanvasBackground,
        surface = SurfaceWhite,
        surfaceVariant = Color(0xFFF3F4F6),
        outline = HairlineBorder,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = InkPrimary,
        onSurface = InkPrimary
    )

    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
