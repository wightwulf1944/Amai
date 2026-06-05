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
import i.am.shiro.amai.network.Nhentai.Sort

@Composable
fun BrowseSortDialog(
    onDismissRequest: () -> Unit,
    onSortChanged: (Sort) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.sort_by))
        },
        text = {
            Column {
                val sortOptions = stringArrayResource(R.array.sort_items_nhentai)
                sortOptions.forEachIndexed { index, optionLabel ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val sort = when (index) {
                                0 -> Sort.DATE
                                1 -> Sort.POPULAR
                                2 -> Sort.POPULAR_TODAY
                                3 -> Sort.POPULAR_WEEK
                                4 -> Sort.POPULAR_MONTH
                                else -> Sort.DATE
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
