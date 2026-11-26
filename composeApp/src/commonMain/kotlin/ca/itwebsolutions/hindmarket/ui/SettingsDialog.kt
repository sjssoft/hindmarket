package ca.itwebsolutions.hindmarket.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.itwebsolutions.hindmarket.model.FontScalePreset

@Composable
fun SettingsDialog(
    currentFontScale: FontScalePreset,
    currentGridColumns: Int,
    showHindiSubtitle: Boolean,
    onFontScaleChange: (FontScalePreset) -> Unit,
    onGridColumnsChange: (Int) -> Unit,
    onToggleHindi: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("App Settings ^& Accessibility") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Text Size (Senior Friendly)", style = MaterialTheme.typography.titleMedium)
                FontScalePreset.entries.forEach { preset ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentFontScale == preset,
                            onClick = { onFontScaleChange(preset) }
                        )
                        Text(preset.label, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                HorizontalDivider()
                Text("Category Grid Columns", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = currentGridColumns == 2,
                        onClick = { onGridColumnsChange(2) },
                        label = { Text("2 Per Row (Large)") }
                    )
                    FilterChip(
                        selected = currentGridColumns == 3,
                        onClick = { onGridColumnsChange(3) },
                        label = { Text("3 Per Row (Compact)") }
                    )
                }

                HorizontalDivider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Bilingual Hindi Labels", style = MaterialTheme.typography.bodyLarge)
                    Switch(checked = showHindiSubtitle, onCheckedChange = onToggleHindi)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Done") }
        }
    )
}
