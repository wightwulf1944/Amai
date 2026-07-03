package i.am.shiro.amai.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.model.FavoritesSort
import i.am.shiro.amai.ui.common.SortMenu

@Composable
fun FavoritesSortMenu(
    selected: FavoritesSort,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSortChanged: (FavoritesSort) -> Unit
) {
    SortMenu(
        selected = selected,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        onSortChanged = onSortChanged,
        sortOptions = listOf(
            stringResource(R.string.newest_first) to FavoritesSort.New,
            stringResource(R.string.oldest_first) to FavoritesSort.Old
        ),
    )
}
