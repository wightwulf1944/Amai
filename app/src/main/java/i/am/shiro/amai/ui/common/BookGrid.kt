package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Adaptive
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import i.am.shiro.amai.model.BookPreview

@Composable
fun BookGrid(
    books: LazyPagingItems<BookPreview>,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
    contentPadding: PaddingValues
) {
    LazyVerticalStaggeredGrid(
        columns = Adaptive(128.dp),
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