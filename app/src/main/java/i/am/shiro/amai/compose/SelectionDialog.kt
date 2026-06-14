package i.am.shiro.amai.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> SelectionDialog(
    title: String,
    options: List<Pair<String, T>>,
    onDismissRequest: () -> Unit,
    onSortChanged: (T) -> Unit
) {
    // TODO replace this with simple Dialog composable
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title)
        },
        text = {
            Column {
                options.forEach { (label, value) ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSortChanged(value)
                            onDismissRequest()
                        },
                        content = {
                            Text(text = label)
                        }
                    )
                }
            }
        },
        confirmButton = {}
    )
}