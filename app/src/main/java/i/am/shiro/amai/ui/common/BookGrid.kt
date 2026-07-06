package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Adaptive
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.model.BookPreview

@Composable
fun BookGrid(
    books: List<BookPreview>,
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
            items = books,
            key = { book -> book.bookId }
        ) {
            BookCard(
                book = it,
                onItemClick = onItemClick
            )
        }
    }
}