package com.example.virasatnamma.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cultural brown/gold palette
val BrownDark    = Color(0xFF4E2A04)
val BrownMedium  = Color(0xFF7B4019)
val BrownLight   = Color(0xFFA0522D)
val GoldPrimary  = Color(0xFFC9A84C)
val GoldLight    = Color(0xFFE8CC80)
val Cream        = Color(0xFFFDF6E3)
val CreamDark    = Color(0xFFF5E6C8)
val OnDark       = Color(0xFFFDF6E3)

private val VirasatColorScheme = lightColorScheme(
    primary          = BrownDark,
    onPrimary        = OnDark,
    primaryContainer = BrownMedium,
    onPrimaryContainer = Cream,
    secondary        = GoldPrimary,
    onSecondary      = BrownDark,
    secondaryContainer = GoldLight,
    onSecondaryContainer = BrownDark,
    background       = Cream,
    onBackground     = BrownDark,
    surface          = CreamDark,
    onSurface        = BrownDark,
    surfaceVariant   = Color(0xFFEDD9B4),
    onSurfaceVariant = BrownMedium,
    outline          = GoldPrimary,
    error            = Color(0xFFB00020),
    onError          = Color.White
)

@Composable
fun VirasatNammaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VirasatColorScheme,
        content = content
    )
}
