package i.am.shiro.amai.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ThemeShowcase() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Row {
            ComponentGallery()
            ColorGallery()
        }
    }
}

@Composable
fun ColorGallery() {
    val color = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionHeader("Accent")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ColorSwatch(color.primary, color.onPrimary, "Primary")
                    ColorSwatch(color.onPrimary, color.primary, "On Primary")
                    ColorSwatch(color.primaryContainer, color.onPrimaryContainer, "Primary Container")
                    ColorSwatch(color.onPrimaryContainer, color.primaryContainer, "On Primary Container")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ColorSwatch(color.secondary, color.onSecondary, "Secondary")
                    ColorSwatch(color.onSecondary, color.secondary, "On Secondary")
                    ColorSwatch(color.secondaryContainer, color.onSecondaryContainer, "Secondary Container")
                    ColorSwatch(color.onSecondaryContainer, color.secondaryContainer, "On Secondary Container")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ColorSwatch(color.tertiary, color.onTertiary, "Tertiary")
                    ColorSwatch(color.onTertiary, color.tertiary, "On Tertiary")
                    ColorSwatch(color.tertiaryContainer, color.onTertiaryContainer, "Tertiary Container")
                    ColorSwatch(color.onTertiaryContainer, color.tertiaryContainer, "On Tertiary Container")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ColorSwatch(color.error, color.onError, "Error")
                    ColorSwatch(color.onError, color.error, "On Error")
                    ColorSwatch(color.errorContainer, color.onErrorContainer, "Error Container")
                    ColorSwatch(color.onErrorContainer, color.errorContainer, "On Error Container")
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionHeader("Background & Outline")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ColorSwatch(color.background, color.onBackground, "Background")
                    ColorSwatch(color.onBackground, color.background, "On Background")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlineSwatch(color.outline, "Outline")
                    OutlineSwatch(color.outlineVariant, "Outline Variant")
                }
            }
        }
        SectionHeader("Surface & Container")
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            ColorSwatchMini(color.surface, color.onSurface, "Surface")
            ColorSwatchMini(color.surfaceBright, color.onSurface, "Surface Bright")
            ColorSwatchMini(color.surfaceDim, color.onSurface, "Surface Dim")
            Spacer(Modifier.width(6.dp))
            ColorSwatchMini(color.surfaceContainerLowest, color.onSurface, "Surface Container Lowest")
            ColorSwatchMini(color.surfaceContainerLow, color.onSurface, "Surface Container Low")
            ColorSwatchMini(color.surfaceContainer, color.onSurface, "Surface Container")
            ColorSwatchMini(color.surfaceContainerHigh, color.onSurface, "Surface Container High")
            ColorSwatchMini(color.surfaceContainerHighest, color.onSurface, "Surface Container Highest")
            Spacer(Modifier.width(6.dp))
            ColorSwatch(color.onSurface, color.surface, "On Surface")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            ColorSwatchMini(color.surfaceVariant, color.onSurfaceVariant, "Surface Variant")
            Spacer(Modifier.width(6.dp))
            ColorSwatch(color.onSurfaceVariant, color.surfaceVariant, "On Surface Variant")
        }
        SectionHeader("Inverse Roles")
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ColorSwatch(color.inversePrimary, color.inverseSurface, "Inverse Primary")
            ColorSwatch(color.inverseSurface, color.inverseOnSurface, "Inverse Surface")
            ColorSwatch(color.inverseOnSurface, color.inverseSurface, "Inverse On Surface")
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun ColorSwatch(
    color: Color,
    onColor: Color,
    label: String,
) {
    Text(
        text = label,
        color = onColor,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .size(width = 150.dp, height = 90.dp)
            .shadow(elevation = 4.dp)
            .background(color)
            .padding(8.dp)
    )
}

@Composable
fun ColorSwatchMini(
    color: Color,
    onColor: Color,
    label: String,
) {
    Text(
        text = label,
        color = onColor,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .size(width = 90.dp, height = 90.dp)
            .shadow(elevation = 4.dp)
            .background(color)
            .padding(4.dp)
    )
}

@Composable
fun OutlineSwatch(
    outlineColor: Color,
    label: String,
) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .size(width = 150.dp, height = 90.dp)
            .border(width = 2.dp, color = outlineColor)
            .padding(8.dp)
    )
}

@Composable
fun ComponentGallery() {
    Column(
        modifier = Modifier
            .width(480.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {}) { Text("Filled") }
            ElevatedButton(onClick = {}) { Text("Elevated") }
            OutlinedButton(onClick = {}) { Text("Outlined") }
            TextButton(onClick = {}) { Text("Text Button") }
        }

        // Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = {},
                label = { Text("Assist") }
            )
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text("Filter") }
            )
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text("Filter (Selected)") }
            )
            SuggestionChip(
                onClick = {},
                label = { Text("Suggestion") }
            )
        }

        // Cards
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Standard Card", style = MaterialTheme.typography.titleMedium)
                Text("Uses surface variant colors by default.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Elevated Card", style = MaterialTheme.typography.titleMedium)
                Text("Features container shadowing.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Outlined Card", style = MaterialTheme.typography.titleMedium)
                Text("Features a clear border line.", style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Inputs
        val sampleText = "Sample input text"
        TextField(
            value = sampleText,
            onValueChange = {},
            label = { Text("Standard TextField") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = sampleText,
            onValueChange = {},
            label = { Text("Outlined TextField") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Checkboxes
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = true, onCheckedChange = {})
                    Text("Checked", style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text("Unchecked", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Switches
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = true, onCheckedChange = {})
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("On", style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = false, onCheckedChange = {})
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Off", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Radio Buttons
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = true, onClick = {})
                    Text("Opt 1", style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = false, onClick = {})
                    Text("Opt 2", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(progress = { 0.7f })
            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.weight(1f)
            )
        }

        // FABs
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            FloatingActionButton(onClick = {}) { Text("+") }
            ExtendedFloatingActionButton(
                onClick = {},
                icon = { Text("+") },
                text = { Text("Extended") }
            )
        }

        Snackbar(
            action = { TextButton(onClick = {}) { Text("Action") } },
            content = { Text("Single-line snackbar with action") }
        )
    }
}
