package i.am.shiro.amai.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.TagModel
import i.am.shiro.amai.model.Thumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    model: DetailModel,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onThumbnailClick: (Int) -> Unit,
    onTagClick: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DetailTopAppBar(
                model = model,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                onFavoriteClick = onFavoriteClick,
                scrollBehavior = scrollBehavior
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    model: DetailModel,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = model.title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = stringResource(R.string.share)
                )
            }
            IconButton(onClick = onFavoriteClick) {
                val tint by animateColorAsState(
                    targetValue = if (model.isFavorite) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Icon(
                    painter = painterResource(R.drawable.ic_favorite),
                    contentDescription = stringResource(R.string.favorite),
                    tint = tint
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
        contentPadding = contentPadding + PaddingValues(
            start = 12.dp,
            end = 12.dp,
            bottom = 8.dp
        )
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            DetailHeaderContent(
                model = model,
                onTagClick = onTagClick
            )
        }

        itemsIndexed(model.thumbnails) { index, thumbnail ->
            DetailThumbnailContent(
                thumbnail = thumbnail,
                onClick = { onThumbnailClick(index) }
            )
        }
    }
}

@Preview
@Composable
fun DetailGridContentPreview() {
    AmaiTheme {
        DetailScreen(
            model = sampleModel(),
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {},
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