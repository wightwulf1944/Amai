package i.am.shiro.amai.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.TagModel
import i.am.shiro.amai.model.Thumbnail

@Composable
fun DetailScreen(
    model: DetailModel,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: (Boolean) -> Unit,
    onThumbnailClick: (Int) -> Unit,
    onTagClick: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    FavoriteSnackbarEffect(
        isFavorite = model.isFavorite,
        snackbarHostState = snackbarHostState
    )

    Scaffold(
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
            DetailContent(
                model = model,
                onThumbnailClick = onThumbnailClick,
                onTagClick = onTagClick,
                contentPadding = innerPadding
            )
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
fun DetailTopBar(
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteToggle: (Boolean) -> Unit,
) {
    val background = Brush.verticalGradient(
        listOf(MaterialTheme.colorScheme.surface, Color.Transparent)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape
        ) {
            Row {
                IconButton(onClick = onShareClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = stringResource(R.string.share)
                    )
                }
                IconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = { onFavoriteToggle(it) },
                    colors = IconButtonDefaults.iconToggleButtonColors(
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
    }
}

@Composable
fun DetailContent(
    model: DetailModel,
    onThumbnailClick: (Int) -> Unit,
    onTagClick: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding + PaddingValues(8.dp, 8.dp, 8.dp, 24.dp),
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
            DetailContentThumbnail(
                thumbnail = thumbnail,
                onClick = { onThumbnailClick(index) }
            )
        }
    }
}

@Preview
@Composable
fun DetailTopBarPreview() {
    AmaiTheme {
        Surface {
            DetailTopBar(
                isFavorite = false,
                onBackClick = {},
                onShareClick = {},
                onFavoriteToggle = {}
            )
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    AmaiTheme {
        DetailScreen(
            model = sampleModel(),
            onBackClick = {},
            onShareClick = {},
            onFavoriteToggle = {},
            onThumbnailClick = {},
            onTagClick = {}
        )
    }
}

private fun sampleModel() = DetailModel(
    title = "Amai: The Sweetest Adventure",
    pageCount = 256,
    isFavorite = true,
    artistTags = listOf(TagModel("artist", "shiro"), TagModel("artist", "kuro")),
    groupTags = listOf(TagModel("group", "C86")),
    parodyTags = listOf(TagModel("parody", "Original")),
    characterTags = listOf(
        TagModel("character", "Amai-chan"),
        TagModel("character", "Mochi-kun")
    ),
    languageTags = listOf(
        TagModel("language", "English"),
        TagModel("language", "Japanese")
    ),
    categoryTags = listOf(TagModel("category", "Manga")),
    generalTags = listOf(
        TagModel("tag", "sweet"),
        TagModel("tag", "comedy"),
        TagModel("tag", "romance"),
        TagModel("tag", "slice of life"),
        TagModel("tag", "school life")
    ),
    thumbnails = List(5) { index ->
        Thumbnail(
            aspectRatio = 0.75f,
            url = "https://example.com/thumb/$index.jpg"
        )
    }
)