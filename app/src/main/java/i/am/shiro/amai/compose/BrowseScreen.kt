package i.am.shiro.amai.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Adaptive
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.data.view.CachedPreviewView

@Composable
fun BrowseScreen(
    title: String,
    books: List<CachedPreviewView>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onSortClick: () -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onPositionBind: (Int) -> Unit,
    gridState: LazyStaggeredGridState
) {
    Scaffold(
        topBar = {
            BrowseTopBar(
                title = title,
                onSortClick = onSortClick,
                onSearchClick = onSearchClick
            )
        },
        content = { innerPadding ->
            BrowseContent(
                books = books,
                isLoading = isLoading,
                onRefresh = onRefresh,
                onItemClick = onItemClick,
                onPositionBind = onPositionBind,
                gridState = gridState,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
fun BrowseTopBar(
    title: String,
    onSortClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val background = Brush.verticalGradient(
        listOf(MaterialTheme.colorScheme.surface, Color.Transparent)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape
        ) {
            Row {
                Text(
                    text = title.tokenize(),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
    }
}

@Composable
fun BrowseContent(
    books: List<CachedPreviewView>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Int) -> Unit,
    onPositionBind: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
    contentPadding: PaddingValues
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            columns = Adaptive(150.dp),
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            contentPadding = contentPadding + PaddingValues(
                start = 8.dp,
                end = 8.dp,
                bottom = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            itemsIndexed(
                items = books,
                key = { _, book -> book.bookId }
            ) { index, book ->
                LaunchedEffect(index) {
                    // Notify the viewmodel of the current position
                    onPositionBind(index)
                }

                BrowseItem(
                    book = book,
                    onItemClick = onItemClick
                )
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(4.dp)
            )
        }
    }
}

private fun String.tokenize(): AnnotatedString {
    var searchMode = SearchMode.START
    var startI = -1

    val spans = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    fun addSpan(start: Int, end: Int) {
        spans += AnnotatedString.Range(
            SpanStyle(textDecoration = TextDecoration.Underline),
            start,
            end
        )
    }

    forEachIndexed { i, c ->
        when (searchMode) {
            SearchMode.START -> {
                when (c) {
                    '"' -> {
                        startI = i
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        // unexpected, do nothing
                    }

                    ' ' -> {
                        // stay in START
                    }

                    else -> {
                        startI = i
                        searchMode = SearchMode.END
                    }
                }
            }

            SearchMode.CONTINUE -> {
                when (c) {
                    '"' -> {
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        // unexpected, do nothing
                    }

                    ' ' -> {
                        // unexpected, end current token
                        addSpan(startI, i)
                        searchMode = SearchMode.START
                    }

                    else -> {
                        searchMode = SearchMode.END
                    }
                }
            }

            SearchMode.END -> {
                when (c) {
                    '"' -> {
                        // unexpected, end current token and start new token
                        addSpan(startI, i)
                        startI = i
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        searchMode = SearchMode.CONTINUE
                    }

                    ' ' -> {
                        addSpan(startI, i)
                        searchMode = SearchMode.START
                    }

                    else -> {
                        // stay in END
                    }
                }
            }

            SearchMode.END_QUOTE -> {
                when (c) {
                    '"' -> {
                        addSpan(startI, i + 1) // i + 1 means include current char in span
                        searchMode = SearchMode.START
                    }

                    else -> {
                        // inside quotes: do nothing
                    }
                }
            }
        }
    }
    if (searchMode != SearchMode.START) {
        addSpan(startI, length)
    }

    return AnnotatedString(
        text = this,
        spanStyles = spans
    )
}

private enum class SearchMode {
    START, CONTINUE, END, END_QUOTE
}

@Preview
@Composable
fun BrowseScreenPreview() {
    val mockBooks = List(7) { i ->
        val title = if (i == 2)
            "Sample Book 2 with a longer title Lorem ipsum dolor sit amet, consectetur adipiscing elit "
        else
            "Sample Book Title $i"
        CachedPreviewView(
            bookId = i,
            title = title,
            pageCount = 100 + i,
            thumbnailWidth = 50,
            thumbnailHeight = if (i % 2 == 0) 70 else 30, // Varied heights for staggered effect
            thumbnailUrl = "",
            isFavorite = i % 3 == 0
        )
    }

    AmaiTheme {
        BrowseScreen(
            title = "nhentai tag:\"big breasts\" artist:shiro",
            books = mockBooks,
            isLoading = false,
            onRefresh = {},
            onSortClick = {},
            onSearchClick = {},
            onItemClick = {},
            onPositionBind = {},
            gridState = rememberLazyStaggeredGridState()
        )
    }
}
