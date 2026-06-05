package i.am.shiro.amai.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.FavoritesSort

@Composable
fun FavoriteSortDialog(
    onDismissRequest: () -> Unit,
    onSortChanged: (FavoritesSort) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.sort_by))
        },
        text = {
            Column {
                val sortOptions = stringArrayResource(R.array.sort_items_favorites)
                sortOptions.forEachIndexed { index, optionLabel ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val sort = when (index) {
                                0 -> FavoritesSort.New
                                1 -> FavoritesSort.Old
                                else -> FavoritesSort.New
                            }
                            onSortChanged(sort)
                            onDismissRequest()
                        },
                        content = {
                            Text(text = optionLabel)
                        }
                    )
                }
            }
        },
        confirmButton = {}
    )
}
