package at.florianschuster.byw.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import at.florianschuster.byw.R

@Composable
fun BYWTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            with(WindowCompat.getInsetsController(window, view)) {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(12.dp),
            small = RoundedCornerShape(16.dp),
            medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(28.dp)
        ),
        typography = with(Typography()) {
            val merriWeatherFontFamily = FontFamily(
                Font(R.font.merriweather_regular)
            )
            val robotoFontFamily = FontFamily(
                Font(R.font.roboto_regular)
            )
            copy(
                displayLarge.copy(fontFamily = merriWeatherFontFamily),
                displayMedium.copy(fontFamily = merriWeatherFontFamily),
                displaySmall.copy(fontFamily = merriWeatherFontFamily),
                headlineLarge.copy(fontFamily = merriWeatherFontFamily),
                headlineMedium.copy(fontFamily = merriWeatherFontFamily),
                headlineSmall.copy(fontFamily = merriWeatherFontFamily),
                titleLarge.copy(fontFamily = robotoFontFamily),
                titleMedium.copy(fontFamily = robotoFontFamily),
                titleSmall.copy(fontFamily = robotoFontFamily),
                bodyLarge.copy(fontFamily = robotoFontFamily),
                bodyMedium.copy(fontFamily = robotoFontFamily),
                bodySmall.copy(fontFamily = robotoFontFamily),
                labelLarge.copy(fontFamily = robotoFontFamily),
                labelMedium.copy(fontFamily = robotoFontFamily),
                labelSmall.copy(fontFamily = robotoFontFamily)
            )
        },
        content = content
    )
}

// following is created via https://m3.material.io/theme-builder#/custom

val md_theme_light_primary = Color(0xFF9C4331)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDAD3)
val md_theme_light_onPrimaryContainer = Color(0xFF3E0400)
val md_theme_light_secondary = Color(0xFF006780)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFB8EAFF)
val md_theme_light_onSecondaryContainer = Color(0xFF001F28)
val md_theme_light_tertiary = Color(0xFF934B00)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFDCC5)
val md_theme_light_onTertiaryContainer = Color(0xFF301400)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF3C0028)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF3C0028)
val md_theme_light_surfaceVariant = Color(0xFFF5DDD9)
val md_theme_light_onSurfaceVariant = Color(0xFF534340)
val md_theme_light_outline = Color(0xFF85736F)
val md_theme_light_inverseOnSurface = Color(0xFFFFECF2)
val md_theme_light_inverseSurface = Color(0xFF5B113F)
val md_theme_light_inversePrimary = Color(0xFFFFB4A5)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF9C4331)
val md_theme_light_outlineVariant = Color(0xFFD8C2BD)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFFFB4A5)
val md_theme_dark_onPrimary = Color(0xFF5F1609)
val md_theme_dark_primaryContainer = Color(0xFF7D2C1D)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD3)
val md_theme_dark_secondary = Color(0xFF5ED4FC)
val md_theme_dark_onSecondary = Color(0xFF003544)
val md_theme_dark_secondaryContainer = Color(0xFF004D61)
val md_theme_dark_onSecondaryContainer = Color(0xFFB8EAFF)
val md_theme_dark_tertiary = Color(0xFFFFB782)
val md_theme_dark_onTertiary = Color(0xFF4F2500)
val md_theme_dark_tertiaryContainer = Color(0xFF703800)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFDCC5)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF3C0028)
val md_theme_dark_onBackground = Color(0xFFFFD8E8)
val md_theme_dark_surface = Color(0xFF3C0028)
val md_theme_dark_onSurface = Color(0xFFFFD8E8)
val md_theme_dark_surfaceVariant = Color(0xFF534340)
val md_theme_dark_onSurfaceVariant = Color(0xFFD8C2BD)
val md_theme_dark_outline = Color(0xFFA08C88)
val md_theme_dark_inverseOnSurface = Color(0xFF3C0028)
val md_theme_dark_inverseSurface = Color(0xFFFFD8E8)
val md_theme_dark_inversePrimary = Color(0xFF9C4331)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFFFB4A5)
val md_theme_dark_outlineVariant = Color(0xFF534340)
val md_theme_dark_scrim = Color(0xFF000000)

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim
)
