package com.example.mycoolmusicplayer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Theme.kt
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val JukeboxColorScheme = lightColorScheme(
    primary = JukeboxRed,
    onPrimary = JukeboxChrome,
    secondary = JukeboxTeal,
    onSecondary = JukeboxBlack,
    background = JukeboxBlack,
    onBackground = JukeboxChrome,
    surface = JukeboxChrome,
    onSurface = JukeboxBlack
)

@Composable
fun MyCoolMusicPlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = JukeboxColorScheme,
        typography = Typography(
            displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 32.sp),
            // Set other text styles as needed
        ),
        content = content
    )
}