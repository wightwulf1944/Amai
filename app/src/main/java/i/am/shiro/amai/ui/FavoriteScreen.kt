package i.am.shiro.amai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import i.am.shiro.amai.R
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.model.FavoritesSort
import i.am.shiro.amai.ui.common.BookGrid
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.theme.AmaiTheme
import i.am.shiro.amai.ui.viewmodel.FavoritesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    onItemClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val books by viewModel.favoriteBooks.collectAsState()
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
    books: List<BookPreview>,
    onSortChanged: (FavoritesSort) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState
) {
    Scaffold(
        topBar = {
            FavoriteTopBar(
                onSearchSubmit = onSearchSubmit,
                onSortChanged = onSortChanged
            )
        },
        content = { innerPadding ->
            BookGrid(
                books = books,
                onItemClick = onItemClick,
                gridState = gridState,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
private fun FavoriteTopBar(
    onSearchSubmit: (String) -> Unit,
    onSortChanged: (FavoritesSort) -> Unit
) {
    TopBarContainer {
        TopBarPill(modifier = Modifier.weight(1f)) {
            SearchInput(onSearchSubmit = onSearchSubmit)
        }
        TopBarPill {
            // synced with FavoritesViewModel
            var sort by remember { mutableStateOf(FavoritesSort.New) }
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = stringResource(R.string.sort)
                )
            }
            FavoritesSortMenu(
                selected = sort,
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onSortChanged = {
                    sort = it
                    onSortChanged(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchInput(
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

    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxSize(),
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
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalContentColor.current.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        }
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
        BookPreview(
            bookId = i,
            title = title,
            pageCount = 100 + i,
            aspectRatio = if (i % 2 == 0) 50f / 70f else 50f / 30f,
            thumbnailPath = "",
            showFavoriteBadge = false
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
