package i.am.shiro.amai.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import i.am.shiro.amai.R
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.ui.common.BookCard
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.viewmodel.NhentaiLatestViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NhentaiLatestGallery(
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: NhentaiLatestViewModel = koinViewModel()
) {
    val books = viewModel.books.collectAsLazyPagingItems()

    val gridState = rememberLazyStaggeredGridState()

    LatestGalleryContent(
        books = books,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        gridState = gridState
    )
}

@Composable
private fun LatestGalleryContent(
    books: LazyPagingItems<BookPreview>,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
) {
    Scaffold(
        topBar = {
            LatestGalleryTopBar(
                onSearchClick = onSearchClick
            )
        },
        content = { innerPadding ->
            PagingGalleryBody(
                books = books,
                onItemClick = onItemClick,
                gridState = gridState,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
private fun LatestGalleryTopBar(
    onSearchClick: () -> Unit
) {
    TopBarContainer {
        TopBarPill {
            Text(
                text = stringResource(R.string.latest),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
            )
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
    }
}

@Composable
fun PagingGalleryBody(
    books: LazyPagingItems<BookPreview>,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
    contentPadding: PaddingValues
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { books.refresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        PagingBookGrid(
            books = books,
            onItemClick = onItemClick,
            gridState = gridState,
            contentPadding = contentPadding
        )

        if (!books.loadState.isIdle) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(4.dp)
            )
        }
    }
}

@Composable
fun PagingBookGrid(
    books: LazyPagingItems<BookPreview>,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
    contentPadding: PaddingValues
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp),
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        contentPadding = contentPadding + PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(
            count = books.itemCount,
            key = books.itemKey { it.bookId }
        ) { index ->
            val book = books[index]
            if (book != null) {
                BookCard(
                    book = book,
                    onItemClick = onItemClick,
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}
