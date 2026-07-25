package i.am.shiro.amai.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import i.am.shiro.amai.R
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.model.FavoritesSort
import i.am.shiro.amai.ui.common.BookGrid
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.theme.AmaiTheme
import i.am.shiro.amai.ui.viewmodel.FavoritesViewModel
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    onItemClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val books = viewModel.books.collectAsLazyPagingItems()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()

    FavoritesContent(
        books = books,
        query = query,
        sort = sort,
        onQueryChange = viewModel::onQueryChange,
        onSortChange = viewModel::onSortChange,
        onItemClick = onItemClick,
    )
}

@Composable
fun FavoritesContent(
    books: LazyPagingItems<BookPreview>,
    query: String,
    sort: FavoritesSort,
    onQueryChange: (String) -> Unit,
    onSortChange: (FavoritesSort) -> Unit,
    onItemClick: (Int) -> Unit,
) {
    val gridState = rememberLazyStaggeredGridState()
    var oldQuery by remember { mutableStateOf(query) }
    var oldSort by remember { mutableStateOf(sort) }
    LaunchedEffect(books.itemCount) {
        if (query != oldQuery || sort != oldSort) {
            gridState.scrollToItem(0)
            oldQuery = query
            oldSort = sort
        }
    }

    Scaffold(
        topBar = {
            FavoriteTopBar(
                query = query,
                sort = sort,
                onQueryChange = onQueryChange,
                onSortChange = onSortChange,
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
    query: String,
    sort: FavoritesSort,
    onQueryChange: (String) -> Unit,
    onSortChange: (FavoritesSort) -> Unit
) {
    TopBarContainer {
        TopBarPill(modifier = Modifier.weight(1f)) {
            SearchInput(
                query = query,
                onQueryChange = onQueryChange
            )
        }
        TopBarPill {
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
                onSortChange = onSortChange
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(isImeVisible) {
        if (!isImeVisible) {
            focusManager.clearFocus()
        }
    }

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxSize(),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = LocalContentColor.current
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = LocalContentColor.current.copy(alpha = 0.5f)
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
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
        val books = flowOf(PagingData.from(mockBooks)).collectAsLazyPagingItems()

        FavoritesContent(
            books = books,
            query = "",
            sort = FavoritesSort.New,
            onQueryChange = {},
            onSortChange = {},
            onItemClick = {},
        )
    }
}
