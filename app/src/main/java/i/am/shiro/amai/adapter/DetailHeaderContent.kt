package i.am.shiro.amai.adapter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.TagModel

@Composable
fun DetailHeaderContent(
    model: DetailModel,
    onTagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = model.title,
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp),
            thickness = 1.dp
        )

        Text(
            text = stringResource(R.string.pages_format, model.pageCount),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Define the order and labels for your tag groups
        val groups = listOf(
            R.string.artists to model.artistTags,
            R.string.groups to model.groupTags,
            R.string.parodies to model.parodyTags,
            R.string.characters to model.characterTags,
            R.string.language to model.languageTags,
            R.string.categories to model.categoryTags,
            R.string.tags to model.generalTags
        )

        groups.forEach { (resId, tags) ->
            if (tags != null) {
                TagGroup(
                    label = stringResource(resId),
                    tags = tags,
                    onTagClick = onTagClick
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun TagGroup(
    label: String,
    tags: List<TagModel>,
    onTagClick: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .clickable { onTagClick(tag.query) }
                    .padding(vertical = 4.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailHeaderContentPreview() {
    val sampleModel = DetailModel(
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
        categoryTags = listOf(TagModel("category:manga", "Manga")),
        generalTags = listOf(
            TagModel("tag", "sweet"),
            TagModel("tag", "comedy"),
            TagModel("tag", "romance"),
            TagModel("tag", "slice of life"),
            TagModel("tag", "school life")
        ),
        thumbnails = emptyList()
    )
    AmaiTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DetailHeaderContent(
                model = sampleModel,
                onTagClick = {}
            )
        }
    }
}
