package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import i.am.shiro.amai.model.BookPreview

@Composable
fun GalleryBody(
    books: LazyPagingItems<BookPreview>,
    onItemClick: (Int) -> Unit,
    contentPadding: PaddingValues
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = books::refresh,
        modifier = Modifier.fillMaxSize()
    ) {
        BookGrid(
            books = books,
            onItemClick = onItemClick,
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
