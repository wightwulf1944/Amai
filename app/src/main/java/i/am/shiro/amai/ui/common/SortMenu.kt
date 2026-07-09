package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R

@Composable
fun <T> SortMenu(
    selected: T,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSortChange: (T) -> Unit,
    sortOptions: List<Pair<String, T>>
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        sortOptions.forEach { (string, sort) ->
            DropdownMenuItem(
                leadingIcon = {
                    if (sort == selected)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                },
                text = { Text(string) },
                onClick = {
                    onSortChange(sort)
                    onDismissRequest()
                }
            )
        }
    }
}