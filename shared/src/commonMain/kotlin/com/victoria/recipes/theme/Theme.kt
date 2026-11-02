package com.victoria.recipes.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.victoria.recipes.domain.model.ThemeMode

private val LightColors = lightColorScheme(
    primary = Terracotta,
    onPrimary = Color.White,
    primaryContainer = Sand,
    onPrimaryContainer = Chocolate,
    secondary = Peach,
    onSecondary = Chocolate,
    secondaryContainer = SandDeep,
    onSecondaryContainer = Chocolate,
    tertiary = Peach,
    onTertiary = Chocolate,
    background = Cream,
    onBackground = Chocolate,
    surface = CreamSurface,
    onSurface = Chocolate,
    surfaceVariant = Sand,
    onSurfaceVariant = Cocoa,
    surfaceContainer = Sand,
    outline = Latte,
    outlineVariant = Latte,
    error = ErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = ErrorLight,
)

private val DarkColors = darkColorScheme(
    primary = Peach,
    onPrimary = OnPeach,
    primaryContainer = Mocha,
    onPrimaryContainer = Milk,
    secondary = Peach,
    onSecondary = OnPeach,
    secondaryContainer = MochaDeep,
    onSecondaryContainer = Milk,
    tertiary = Peach,
    onTertiary = OnPeach,
    background = Night,
    onBackground = Milk,
    surface = Espresso,
    onSurface = Milk,
    surfaceVariant = Mocha,
    onSurfaceVariant = Caramel,
    surfaceContainer = Mocha,
    outline = Bark,
    outlineVariant = Bark,
    error = ErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = ErrorDark,
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

val LocalDarkTheme = staticCompositionLocalOf { false }

@Composable
fun RecipesTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    CompositionLocalProvider(LocalDarkTheme provides dark) {
        MaterialTheme(
            colorScheme = if (dark) DarkColors else LightColors,
            typography = appTypography(),
            shapes = AppShapes,
            content = content,
        )
    }
}
