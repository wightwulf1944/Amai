package i.am.shiro.amai.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.data.remote.Nhentai.Sort

// TODO replace this with a context menu
@Composable
fun GallerySortDialog(
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
