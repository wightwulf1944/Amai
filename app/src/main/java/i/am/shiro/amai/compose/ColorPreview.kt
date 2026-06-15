package i.am.shiro.amai.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorPreview() {
    val color = MaterialTheme.colorScheme
    Column {
        Row {
            ColorSwatch(color.background, color.onBackground, "Background")
            ColorSwatch(color.onBackground, color.background, "On Background")
        }
        Row {
            ColorSwatch(color.primary, color.onPrimary, "Primary")
            ColorSwatch(color.secondary, color.onSecondary, "Secondary")
            ColorSwatch(color.tertiary, color.onTertiary, "Tertiary")
            ColorSwatch(color.error, color.onError, "Error")
        }
        Row {
            ColorSwatch(color.onPrimary, color.primary, "On Primary")
            ColorSwatch(color.onSecondary, color.secondary, "On Secondary")
            ColorSwatch(color.onTertiary, color.tertiary, "On Tertiary")
            ColorSwatch(color.onError, color.error, "On Error")
        }
        Row {
            ColorSwatch(color.primaryContainer, color.onPrimaryContainer, "Primary Container")
            ColorSwatch(color.secondaryContainer, color.onSecondaryContainer, "Secondary Container")
            ColorSwatch(color.tertiaryContainer, color.onTertiaryContainer, "Tertiary Container")
            ColorSwatch(color.errorContainer, color.onErrorContainer, "Error Container")
        }
        Row {
            ColorSwatch(color.onPrimaryContainer, color.primaryContainer, "On Primary Container")
            ColorSwatch(color.onSecondaryContainer, color.secondaryContainer, "On Secondary Container")
            ColorSwatch(color.onTertiaryContainer, color.tertiaryContainer, "On Tertiary Container")
            ColorSwatch(color.onErrorContainer, color.errorContainer, "On Error Container")
        }
        Row {
            ColorSwatch(color.surfaceDim, color.onSurface, "Surface Dim")
            ColorSwatch(color.surface, color.onSurface, "Surface")
            ColorSwatch(color.surfaceBright, color.onSurface, "Surface Bright")
            ColorSwatch(color.surfaceVariant, color.onSurfaceVariant, "Surface Variant")
        }
        Row {
            ColorSwatch(color.surfaceContainerLowest, color.onSurface, "Surface Container Lowest")
            ColorSwatch(color.surfaceContainerLow, color.onSurface, "Surface Container Low")
            ColorSwatch(color.surfaceContainer, color.onSurface, "Surface Container")
            ColorSwatch(color.surfaceContainerHigh, color.onSurface, "Surface Container High")
            ColorSwatch(color.surfaceContainerHighest, color.onSurface, "Surface Container Highest")
        }
        Row {
            ColorSwatch(color.onSurface, color.surface, "On Surface")
            ColorSwatch(color.onSurfaceVariant, color.surfaceVariant, "On Surface Variant")
        }
        Row {
            ColorSwatch(color.outline, color.surface, "Outline")
            ColorSwatch(color.outlineVariant, color.onSurface, "Outline Variant")
        }
        Row {
            ColorSwatch(color.inverseSurface, color.inverseOnSurface, "Inverse Surface")
            ColorSwatch(color.inversePrimary, color.primary, "Inverse Primary")
            ColorSwatch(color.inverseOnSurface, color.inverseSurface, "Inverse On Surf")
        }
    }
}

@Composable
fun RowScope.ColorSwatch(
    color: Color,
    onColor: Color,
    label: String
) {
    Text(
        text = label,
        color = onColor,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .weight(0.5f)
            .height(64.dp)
            .background(color)
            .padding(8.dp)
    )
}

@Preview(widthDp = 600)
@Composable
fun ColorPreviewPreview() {
    AmaiTheme {
        ColorPreview()
    }
}
