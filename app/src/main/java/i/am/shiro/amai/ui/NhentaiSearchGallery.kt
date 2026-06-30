package i.am.shiro.amai.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import i.am.shiro.amai.R
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.theme.AmaiTheme
import i.am.shiro.amai.ui.utils.asSymmetricHorizontal
import i.am.shiro.amai.ui.viewmodel.NhentaiSearchViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// TODO try jetpack paging library for loading content
@Composable
fun NhentaiSearchGallery(
    searchQuery: String,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: NhentaiSearchViewModel = koinViewModel {
        parametersOf(searchQuery)
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
        title = stringResource(R.string.search_format, searchQuery.tokenize()),
        books = books,
        isLoading = viewModel.isLoading,
        onRefresh = viewModel::refresh,
        onSortChanged = viewModel::sort,
        onSearchClick = onSearchClick,
        onItemClick = onItemClick,
        gridState = gridState
    )
}

@Composable
fun GalleryContent(
    title: String,
    books: List<BookPreview>,
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
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.asSymmetricHorizontal(0.5f),
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
                text = title,
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
fun GalleryContentPreview() {
    val mockBooks = List(7) { i ->
        val title = if (i == 2)
            "Sample Book 2 with a longer title Lorem ipsum dolor sit amet, consectetur adipiscing elit "
        else
            "Sample Book Title $i"
        BookPreview(
            bookId = i,
            title = title,
            pageCount = 100 + i,
            aspectRatio = if (i % 2 == 0) 50f / 70f else 50f / 30f,
            thumbnailPath = "",
            showFavoriteBadge = i % 3 == 0
        )
    }

    AmaiTheme {
        GalleryContent(
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
