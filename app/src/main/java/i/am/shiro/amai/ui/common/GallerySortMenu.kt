package i.am.shiro.amai.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.data.remote.Nhentai

@Composable
fun GallerySortMenu(
    selected: Nhentai.Sort,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSortChanged: (Nhentai.Sort) -> Unit
) {
    SortMenu(
        selected = selected,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        onSortChanged = onSortChanged,
        sortOptions = listOf(
            stringResource(R.string.newest) to Nhentai.Sort.DATE,
            stringResource(R.string.popular) to Nhentai.Sort.POPULAR,
            stringResource(R.string.popular_today) to Nhentai.Sort.POPULAR_TODAY,
            stringResource(R.string.popular_week) to Nhentai.Sort.POPULAR_WEEK,
            stringResource(R.string.popular_month) to Nhentai.Sort.POPULAR_MONTH
        ),
    )
}
