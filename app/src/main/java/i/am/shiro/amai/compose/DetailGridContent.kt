package i.am.shiro.amai.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.TagModel
import i.am.shiro.amai.model.Thumbnail

@Composable
fun DetailGridContent(
    model: DetailModel,
    onThumbnailClick: (Int) -> Unit,
    onTagClick: (String) -> Unit,
    contentPadding: PaddingValues
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollInterop),
        contentPadding = contentPadding
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
        Surface(color = MaterialTheme.colorScheme.background) {
            DetailGridContent(
                model = sampleModel(),
                onThumbnailClick = {},
                onTagClick = {},
                contentPadding = PaddingValues(12.dp, 8.dp)
            )
        }
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
            width = 200,
            height = 300,
            url = "https://example.com/thumb/$index.jpg"
        )
    }
)