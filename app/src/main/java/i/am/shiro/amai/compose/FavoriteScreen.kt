package i.am.shiro.amai.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.R
import i.am.shiro.amai.compose.common.AmaiTheme
import i.am.shiro.amai.compose.common.TopBarContainer
import i.am.shiro.amai.compose.common.TopBarPill
import i.am.shiro.amai.data.view.FavoritesPreviewView
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.viewmodel.FavoritesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    onItemClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val books by viewModel.books.collectAsState()
    val gridState = rememberLazyStaggeredGridState()
    var shouldScrollToTop by remember { mutableStateOf(false) }

    LaunchedEffect(books) {
        if (shouldScrollToTop) {
            gridState.scrollToItem(0)
            shouldScrollToTop = false
        }
    }

    FavoritesContent(
        books = books,
        onSortChanged = { sort ->
            shouldScrollToTop = true
            viewModel.onSort(sort)
        },
        onSearchSubmit = { searchQuery ->
            shouldScrollToTop = true
            viewModel.onSearch(searchQuery)
        },
        onItemClick = onItemClick,
        gridState = gridState
    )
}

@Composable
fun FavoritesContent(
    books: List<FavoritesPreviewView>,
    onSortChanged: (FavoritesSort) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState
) {
    var showSortDialog by remember { mutableStateOf(false) }

    if (showSortDialog) {
        FavoriteSortDialog(
            onDismissRequest = { showSortDialog = false },
            onSortChanged = onSortChanged
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            FavoriteTopBar(
                onSearchSubmit = onSearchSubmit,
                onSortClick = { showSortDialog = true }
            )
        },
        content = { innerPadding ->
            FavoritesBody(
                books = books,
                onItemClick = onItemClick,
                gridState = gridState,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
fun FavoriteTopBar(
    onSearchSubmit: (String) -> Unit,
    onSortClick: () -> Unit
) {
    TopBarContainer {
        TopBarPill {
            SearchInput(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp),
                onSearchSubmit = onSearchSubmit
            )

            IconButton(onClick = onSortClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = stringResource(R.string.sort)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchInput(
    modifier: Modifier = Modifier,
    onSearchSubmit: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(isImeVisible) {
        if (!isImeVisible) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        if (text.isEmpty()) {
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.bodyLarge,
                color = LocalContentColor.current.copy(alpha = 0.5f)
            )
        }
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = LocalContentColor.current
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions {
                onSearchSubmit(text)
                focusManager.clearFocus()
            },
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun FavoritesBody(
    books: List<FavoritesPreviewView>,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
    contentPadding: PaddingValues
) {
    val books = remember(books) {
        books.map {
            BookPreview(
                bookId = it.bookId,
                aspectRatio = it.thumbnailWidth.toFloat() / it.thumbnailHeight.toFloat(),
                thumbnailUrl = it.thumbnailUrl,
                title = it.title,
                showFavoriteBadge = false,
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
}

@Preview
@Composable
fun FavoritesContentPreview() {
    val mockBooks = List(7) { i ->
        val title = if (i == 2)
            "Sample Book 2 with a longer title Lorem ipsum dolor sit amet, consectetur adipiscing elit "
        else
            "Sample Book Title $i"
        FavoritesPreviewView(
            bookId = i,
            favoriteDate = 0L,
            title = title,
            pageCount = 100 + i,
            thumbnailWidth = 50,
            thumbnailHeight = if (i % 2 == 0) 70 else 30, // Varied heights for staggered effect
            thumbnailUrl = ""
        )
    }

    AmaiTheme {
        FavoritesContent(
            books = mockBooks,
            onSortChanged = {},
            onSearchSubmit = {},
            onItemClick = {},
            gridState = rememberLazyStaggeredGridState()
        )
    }
}
