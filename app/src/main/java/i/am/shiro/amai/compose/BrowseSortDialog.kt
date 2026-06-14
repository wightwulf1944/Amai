package i.am.shiro.amai.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.network.Nhentai.Sort

@Composable
fun BrowseSortDialog(
    onDismissRequest: () -> Unit,
    onSortChanged: (Sort) -> Unit
) {
    SelectionDialog(
        title = stringResource(R.string.sort_by),
        options = listOf(
            stringResource(R.string.newest) to Sort.DATE,
            stringResource(R.string.popular) to Sort.POPULAR,
            stringResource(R.string.popular_today) to Sort.POPULAR_TODAY,
            stringResource(R.string.popular_week) to Sort.POPULAR_WEEK,
            stringResource(R.string.popular_month) to Sort.POPULAR_MONTH
        ),
        onDismissRequest = onDismissRequest,
        onSortChanged = onSortChanged
    )
}
