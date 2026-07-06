package i.am.shiro.amai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fitOutside
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.WindowInsetsRulers.Companion.NavigationBars
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.model.BookDetail
import i.am.shiro.amai.model.Tag
import i.am.shiro.amai.model.Thumbnail
import i.am.shiro.amai.ui.common.AmaiScaffold
import i.am.shiro.amai.ui.common.BookThumbnail
import i.am.shiro.amai.ui.common.TopBarContainer
import i.am.shiro.amai.ui.common.TopBarPill
import i.am.shiro.amai.ui.theme.AmaiTheme
import i.am.shiro.amai.ui.utils.Toggle
import i.am.shiro.amai.ui.utils.union
import i.am.shiro.amai.ui.viewmodel.DetailViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    bookId: Int,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onThumbnailClick: (Int) -> Unit,
    onTagClick: (Tag) -> Unit,
    viewModel: DetailViewModel = koinViewModel {
        parametersOf(bookId)
    }
) {
    val model by viewModel.uiState.collectAsState()
    model?.let {
        DetailContent(
            model = it,
            onBackClick = onBackClick,
            onShareClick = onShareClick,
            onFavoriteToggle = viewModel::onFavoriteToggle,
            onThumbnailClick = onThumbnailClick,
            onTagClick = onTagClick
        )
    }
}

@Composable
fun DetailContent(
    model: BookDetail,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: (Boolean) -> Unit,
    onThumbnailClick: (Int) -> Unit,
    onTagClick: (Tag) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    FavoriteSnackbarEffect(
        isFavorite = model.isFavorite,
        snackbarHostState = snackbarHostState
    )

    AmaiScaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            DetailTopBar(
                isFavorite = model.isFavorite,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                onFavoriteToggle = onFavoriteToggle
            )
        },
        content = { innerPadding ->
            DetailBody(
                model = model,
                onThumbnailClick = onThumbnailClick,
                onTagClick = onTagClick,
                contentPadding = innerPadding
            )
            NavigationBarScrim()
        }
    )
}

@Composable
fun FavoriteSnackbarEffect(
    isFavorite: Boolean,
    snackbarHostState: SnackbarHostState
) {
    var isFirstComposition by remember { mutableStateOf(true) }

    val addedMessage = stringResource(R.string.added_to_favorites)
    val removedMessage = stringResource(R.string.removed_from_favorites)

    LaunchedEffect(isFavorite) {
        if (isFirstComposition) {
            isFirstComposition = false
        } else {
            snackbarHostState.showSnackbar(
                message = if (isFavorite) addedMessage else removedMessage
            )
        }
    }
}

@Composable
fun NavigationBarScrim() = Spacer(
    Modifier
        .fillMaxSize()
        .fitOutside(NavigationBars.current)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    MaterialTheme.colorScheme.background
                )
            )
        )
)

@Composable
private fun RowScope.DetailTopBar(
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: (Boolean) -> Unit,
) {
    TopBarPill {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back)
            )
        }
    }

    Spacer(modifier = Modifier.weight(1f))

    TopBarPill {
        IconButton(onClick = onShareClick) {
            Icon(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = stringResource(R.string.share)
            )
        }
        val haptic = LocalHapticFeedback.current
        FilledTonalIconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                haptic.performHapticFeedback(HapticFeedbackType.Toggle(it))
                onFavoriteToggle(it)
            },
            colors = IconButtonDefaults.filledTonalIconToggleButtonColors(
                checkedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                checkedContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_favorite),
                contentDescription = stringResource(R.string.favorite),
            )
        }
    }
}

@Composable
fun DetailBody(
    model: BookDetail,
    onThumbnailClick: (Int) -> Unit,
    onTagClick: (Tag) -> Unit,
    contentPadding: PaddingValues,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
            .plus(PaddingValues(top = 8.dp))
            .union(PaddingValues(8.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            DetailContentHeader(
                model = model,
                onTagClick = onTagClick
            )
        }

        itemsIndexed(model.thumbnails) { index, thumbnail ->
            BookThumbnail(
                path = thumbnail.path,
                aspectRatio = thumbnail.aspectRatio,
                modifier = Modifier.clickable(onClick = { onThumbnailClick(index) })
            )
        }
    }
}

@Preview
@Composable
fun DetailTopBarPreview() {
    AmaiTheme {
        Surface {
            TopBarContainer {
                DetailTopBar(
                    isFavorite = false,
                    onBackClick = {},
                    onShareClick = {},
                    onFavoriteToggle = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailContentPreview() {
    AmaiTheme {
        DetailContent(
            model = sampleModel(),
            onBackClick = {},
            onShareClick = {},
            onFavoriteToggle = {},
            onThumbnailClick = {},
            onTagClick = {}
        )
    }
}

fun sampleModel() = BookDetail(
    title = "Amai: The Sweetest Adventure",
    pageCount = 256,
    isFavorite = true,
    artistTags = listOf(Tag(0, "shiro", ""), Tag(0, "kuro", "")),
    groupTags = listOf(Tag(0, "C86", "")),
    parodyTags = listOf(Tag(0, "Original", "")),
    characterTags = listOf(
        Tag(0, "Amai-chan", ""),
        Tag(0, "Mochi-kun", "")
    ),
    languageTags = listOf(
        Tag(0, "English", ""),
        Tag(0, "Japanese", "")
    ),
    categoryTags = listOf(Tag(0, "Manga", "")),
    generalTags = listOf(
        Tag(0, "sweet", ""),
        Tag(0, "comedy", ""),
        Tag(0, "romance", ""),
        Tag(0, "slice of life", ""),
        Tag(0, "school life", "")
    ),
    thumbnails = List(5) { index ->
        Thumbnail(
            aspectRatio = 0.75f,
            path = "https://example.com/thumb/$index.jpg"
        )
    }
)