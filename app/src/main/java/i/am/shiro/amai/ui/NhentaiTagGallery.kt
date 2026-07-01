package i.am.shiro.amai.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import i.am.shiro.amai.R
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.ui.common.AmaiScaffold
import i.am.shiro.amai.ui.common.GalleryBody
import i.am.shiro.amai.ui.common.GallerySortDialog
import i.am.shiro.amai.ui.common.TopBarPill
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

    TagGalleryContent(
        tagName = tagName,
        books = books,
        isLoading = viewModel.isLoading,
        onRefresh = viewModel::refresh,
        onSortChanged = viewModel::sort,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        gridState = gridState,
    )
}

@Composable
private fun TagGalleryContent(
    tagName: String,
    books: List<BookPreview>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onSortChanged: (Sort) -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
) {
    var showSortDialog by remember { mutableStateOf(false) }

    if (showSortDialog) {
        GallerySortDialog(
            onDismissRequest = { showSortDialog = false },
            onSortChanged = onSortChanged
        )
    }

    AmaiScaffold(
        topBar = {
            TagGalleryTopBar(
                tagName = tagName,
                onSortClick = { showSortDialog = true },
                onSearchClick = onSearchClick
            )
        },
        content = { innerPadding ->
            GalleryBody(
                books = books,
                isLoading = isLoading,
                onRefresh = onRefresh,
                onItemClick = onItemClick,
                gridState = gridState,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
private fun TagGalleryTopBar(
    tagName: String,
    onSortClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopBarPill {
        Row(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.tagged),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .alignByBaseline()
                    .padding(start = 16.dp),
            )
            Text(
                text = tagName,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
                    .padding(start = 6.dp)
            )
        }
        IconButton(onClick = onSortClick) {
            Icon(
                painter = painterResource(R.drawable.ic_sort),
                contentDescription = stringResource(R.string.sort)
            )
        }
        IconButton(onClick = onSearchClick) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = stringResource(R.string.search)
            )
        }
    }
}
