package i.am.shiro.amai.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.model.FavoritesSort
import i.am.shiro.amai.ui.common.SelectionDialog

@Composable
fun FavoriteSortDialog(
    onDismissRequest: () -> Unit,
    onSortChanged: (FavoritesSort) -> Unit
) {
    SelectionDialog(
        title = stringResource(R.string.sort_by),
        options = listOf(
            stringResource(R.string.newest_first) to FavoritesSort.New,
            stringResource(R.string.oldest_first) to FavoritesSort.Old
        ),
        onDismissRequest = onDismissRequest,
        onSortChanged = onSortChanged
    )
}
