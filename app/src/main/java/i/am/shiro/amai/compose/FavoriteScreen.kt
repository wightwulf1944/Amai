package i.am.shiro.amai.compose

import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Adaptive
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import i.am.shiro.amai.R
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.view.FavoritesPreviewView

@Composable
fun FavoritesScreen(
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
        topBar = {
            FavoriteTopBar(
                onSearchSubmit = onSearchSubmit,
                onSortClick = { showSortDialog = true }
            )
        },
        content = { innerPadding ->
            FavoriteContent(
                books = books,
                onItemClick = onItemClick,
                gridState = gridState,
                innerPadding
            )
        }
    )
}

@Composable
fun FavoriteTopBar(
    onSearchSubmit: (String) -> Unit,
    onSortClick: () -> Unit
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                SearchInput(
                    modifier = Modifier.weight(1f),
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
}

@Composable
fun SearchInput(
    modifier: Modifier = Modifier,
    onSearchSubmit: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val isImeVisible by keyboardAsState()

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

// TODO once compose migration is complete and edge-to-edge is implemented, replace this
//  with WindowInsets.isImeVisible
@Composable
fun keyboardAsState(): State<Boolean> {
    val view = LocalView.current
    var isImeVisible by remember { mutableStateOf(false) }

    DisposableEffect(LocalWindowInfo.current) {
        val listener = ViewTreeObserver.OnPreDrawListener {
            val rootWindowInsets = ViewCompat.getRootWindowInsets(view)
            isImeVisible = rootWindowInsets?.isVisible(WindowInsetsCompat.Type.ime()) == true
            true
        }
        view.viewTreeObserver.addOnPreDrawListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun FavoriteContent(
    books: List<FavoritesPreviewView>,
    onItemClick: (Int) -> Unit,
    gridState: LazyStaggeredGridState,
    contentPadding: PaddingValues
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
        items(
            items = books,
            key = { book -> book.bookId }
        ) { book ->
            FavoriteItem(
                book = book,
                onItemClick = onItemClick
            )
        }
    }
}

@Preview
@Composable
fun FavoriteScreenPreview() {
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
        FavoritesScreen(
            books = mockBooks,
            onSortChanged = {},
            onSearchSubmit = {},
            onItemClick = {},
            gridState = rememberLazyStaggeredGridState()
        )
    }
}
