package i.am.shiro.amai.ui

import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import i.am.shiro.amai.R
import i.am.shiro.amai.ui.viewmodel.NhentaiTagViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NhentaiTagGallery(
    tagId: Int,
    tagName: String,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: NhentaiTagViewModel = koinViewModel {
        parametersOf(tagId)
    }
) {
    val books by viewModel.books.collectAsStateWithLifecycle()

    val gridState = rememberLazyStaggeredGridState()

    LaunchedEffect(gridState.canScrollForward) {
        if (!gridState.canScrollForward) {
            viewModel.loadMore()
        }
    }

    GalleryContent(
        title = stringResource(R.string.tagged_format, tagName),
        books = books,
        isLoading = viewModel.isLoading,
        onRefresh = viewModel::refresh,
        onSortChanged = viewModel::sort,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        gridState = gridState
    )
}
