package i.am.shiro.amai.compose.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AmaiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

val colorScheme = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    inversePrimary = inversePrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    surfaceDim = surfaceDim,
    surfaceBright = surfaceBright,
    surfaceVariant = surfaceVariant,
    inverseSurface = inverseSurface,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceVariant,
    inverseOnSurface = inverseOnSurface,
    surfaceContainer = surfaceContainer,
    surfaceContainerLowest = surfaceContainerLowest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    outline = outline,
    outlineVariant = outlineVariant,
)