package i.am.shiro.amai.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.compose.common.AmaiTheme
import i.am.shiro.amai.compose.common.TopBarContainer
import i.am.shiro.amai.compose.common.TopBarPill
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.model.SearchEvent
import i.am.shiro.amai.network.Nhentai.Sort
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import org.koin.compose.viewmodel.koinViewModel

// TODO try jetpack paging library for loading content
@Composable
fun BrowseScreen(
    gridState: LazyStaggeredGridState,
    searchEvent: SearchEvent?,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: NhentaiViewModel = koinViewModel()
) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val initialTitle = stringResource(R.string.nhentai)
    var title by rememberSaveable { mutableStateOf(initialTitle) }

    LaunchedEffect(gridState.canScrollForward) {
        if (!gridState.canScrollForward) {
            viewModel.onScrollToBottom()
        }
    }

    LaunchedEffect(searchEvent) {
        if (searchEvent?.isProcessing == true) {
            title = searchEvent.query
            gridState.scrollToItem(0)
            viewModel.onSearch(searchEvent.query)
            searchEvent.isProcessing = false
        }
    }

    BrowseContent(
        title = title,
        books = books,
        isLoading = isLoading,
        onRefresh = viewModel::onRefresh,
        onSortChanged = viewModel::onSort,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        gridState = gridState
    )
}

@Composable
fun BrowseContent(
    title: String,
    books: List<CachedPreviewView>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onSortChanged: (Sort) -> Unit,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState
) {
    var showSortDialog by remember { mutableStateOf(false) }

    if (showSortDialog) {
        BrowseSortDialog(
            onDismissRequest = { showSortDialog = false },
            onSortChanged = onSortChanged
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            BrowseTopBar(
                title = title,
                onSortClick = { showSortDialog = true },
                onSearchClick = onSearchClick
            )
        },
        content = { innerPadding ->
            BrowseBody(
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
fun BrowseTopBar(
    title: String,
    onSortClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopBarContainer {
        TopBarPill {
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

@Composable
fun BrowseBody(
    books: List<CachedPreviewView>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
    contentPadding: PaddingValues
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        val books = remember(books) {
            books.map {
                BookPreview(
                    bookId = it.bookId,
                    aspectRatio = it.thumbnailWidth.toFloat() / it.thumbnailHeight.toFloat(),
                    thumbnailUrl = it.thumbnailUrl,
                    title = it.title,
                    showFavoriteBadge = it.isFavorite,
                    pageCount = it.pageCount
                )
            }
        }
        BookGrid(
            books = books,
            onItemClick = onItemClick,
            gridState = gridState,
            contentPadding = contentPadding
        )

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
fun BrowseContentPreview() {
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
        BrowseContent(
            title = "nhentai tag:\"big breasts\" artist:shiro",
            books = mockBooks,
            isLoading = false,
            onRefresh = {},
            onSortChanged = {},
            onSearchClick = {},
            onItemClick = {},
            gridState = rememberLazyStaggeredGridState()
        )
    }
}
