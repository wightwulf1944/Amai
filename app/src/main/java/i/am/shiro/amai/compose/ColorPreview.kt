package i.am.shiro.amai.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorPreview() {
    val color = MaterialTheme.colorScheme
    Box(Modifier.background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.2f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ColorSwatch(color.inversePrimary, color.primary, "Inverse Primary", Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(68.dp))
                Spacer(modifier = Modifier.height(68.dp))
                Spacer(modifier = Modifier.height(68.dp))
                ColorSwatch(color.inverseSurface, color.inverseOnSurface, "Inverse Surface", Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(68.dp))
                ColorSwatch(color.inverseOnSurface, color.inverseSurface, "Inv On Surf", Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ColorRow {
                    ColorSwatch(color.primary, color.onPrimary, "Primary", Modifier.weight(1f))
                    ColorSwatch(color.secondary, color.onSecondary, "Secondary", Modifier.weight(1f))
                    ColorSwatch(color.tertiary, color.onTertiary, "Tertiary", Modifier.weight(1f))
                    ColorSwatch(color.error, color.onError, "Error", Modifier.weight(1f))
                }
                ColorRow {
                    ColorSwatch(color.onPrimary, color.primary, "On Primary", Modifier.weight(1f))
                    ColorSwatch(color.onSecondary, color.secondary, "On Secondary", Modifier.weight(1f))
                    ColorSwatch(color.onTertiary, color.tertiary, "On Tertiary", Modifier.weight(1f))
                    ColorSwatch(color.onError, color.error, "On Error", Modifier.weight(1f))
                }

                ColorRow {
                    ColorSwatch(color.primaryContainer, color.onPrimaryContainer, "Primary Container", Modifier.weight(1f))
                    ColorSwatch(color.secondaryContainer, color.onSecondaryContainer, "Secondary Container", Modifier.weight(1f))
                    ColorSwatch(color.tertiaryContainer, color.onTertiaryContainer, "Tertiary Container", Modifier.weight(1f))
                    ColorSwatch(color.errorContainer, color.onErrorContainer, "Error Container", Modifier.weight(1f))
                }
                ColorRow {
                    ColorSwatch(color.onPrimaryContainer, color.primaryContainer, "On Primary Container", Modifier.weight(1f))
                    ColorSwatch(color.onSecondaryContainer, color.secondaryContainer, "On Secondary Container", Modifier.weight(1f))
                    ColorSwatch(color.onTertiaryContainer, color.tertiaryContainer, "On Tertiary Container", Modifier.weight(1f))
                    ColorSwatch(color.onErrorContainer, color.errorContainer, "On Error Container", Modifier.weight(1f))
                }

                ColorRow {
                    ColorSwatch(color.surfaceDim, color.onSurface, "Surface Dim", Modifier.weight(1f))
                    ColorSwatch(color.surface, color.onSurface, "Surface", Modifier.weight(1f))
                    ColorSwatch(color.surfaceBright, color.onSurface, "Surface Bright", Modifier.weight(1f))
                    ColorSwatch(color.surfaceVariant, color.onSurfaceVariant, "Surface Variant", Modifier.weight(1f))
                }

                ColorRow {
                    ColorSwatch(color.surfaceContainerLowest, color.onSurface, "Surface Container Lowest", Modifier.weight(1f))
                    ColorSwatch(color.surfaceContainerLow, color.onSurface, "Surface Container Low", Modifier.weight(1f))
                    ColorSwatch(color.surfaceContainer, color.onSurface, "Surface Container", Modifier.weight(1f))
                    ColorSwatch(color.surfaceContainerHigh, color.onSurface, "Surface Container High", Modifier.weight(1f))
                    ColorSwatch(color.surfaceContainerHighest, color.onSurface, "Surface Container Highest", Modifier.weight(1f))
                }

                ColorRow {
                    ColorSwatch(color.onSurface, color.surface, "On Surface", Modifier.weight(1f))
                    ColorSwatch(color.onSurfaceVariant, color.surfaceVariant, "On Surface Variant", Modifier.weight(1f))
                    ColorSwatch(color.outline, color.surface, "Outline", Modifier.weight(1f))
                    ColorSwatch(color.outlineVariant, color.onSurface, "Outline Variant", Modifier.weight(1f))
                }

                ColorRow {
                    ColorSwatch(color.scrim, Color.White, "Scrim", Modifier.weight(1f))
                    ColorSwatch(Color.Black, Color.White, "Shadow", Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ColorRow(content: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = content
    )
}

@Composable
fun ColorSwatch(
    color: Color,
    onColor: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(68.dp)
            .background(color)
            .padding(8.dp)
    ) {
        Text(
            text = label,
            color = onColor,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}

@Preview(widthDp = 600)
@Composable
fun ColorPreviewPreview() {
    AmaiTheme {
        ColorPreview()
    }
}
