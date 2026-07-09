package i.am.shiro.amai.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import i.am.shiro.amai.R
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.ui.common.GalleryBody
import i.am.shiro.amai.ui.common.GallerySortMenu
import i.am.shiro.amai.ui.common.TopBarContainer
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
        sort = viewModel.sort,
        books = books,
        isLoading = viewModel.isLoading,
        onRefresh = viewModel::refresh,
        onSortChange = viewModel::sort,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        gridState = gridState,
    )
}

@Composable
private fun TagGalleryContent(
    tagName: String,
    sort: Nhentai.Sort,
    books: List<BookPreview>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onSortChange: (Nhentai.Sort) -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
) {
    Scaffold(
        topBar = {
            TagGalleryTopBar(
                tagName = tagName,
                sort = sort,
                onSortChange = onSortChange,
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
    sort: Nhentai.Sort,
    onSortChange: (Nhentai.Sort) -> Unit,
    onSearchClick: () -> Unit
) {
    TopBarContainer {
        TopBarPill(modifier = Modifier.weight(1f)) {
            FlowRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.tagged),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = tagName,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alignByBaseline()
                )
            }
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
        TopBarPill {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = stringResource(R.string.sort)
                )
            }
            GallerySortMenu(
                selected = sort,
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onSortChange = onSortChange
            )
        }
    }
}
