package i.am.shiro.amai.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import i.am.shiro.amai.R
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.ui.common.PagingGalleryBody
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.viewmodel.NhentaiLatestViewModel
import org.koin.compose.viewmodel.koinViewModel

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
