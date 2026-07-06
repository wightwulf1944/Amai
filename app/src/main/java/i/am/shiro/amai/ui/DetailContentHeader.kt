package i.am.shiro.amai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.model.BookDetail
import i.am.shiro.amai.model.Tag
import i.am.shiro.amai.ui.theme.AmaiTheme

@Composable
fun DetailContentHeader(
    model: BookDetail,
    onTagClick: (Tag) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = model.title,
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = pluralStringResource(R.plurals.pages_format, model.pageCount, model.pageCount),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        listOf(
            R.string.artists to model.artistTags,
            R.string.groups to model.groupTags,
            R.string.parodies to model.parodyTags,
            R.string.characters to model.characterTags,
            R.string.language to model.languageTags,
            R.string.categories to model.categoryTags,
            R.string.tags to model.generalTags
        ).forEach { (resId, tags) ->
            TagGroup(
                label = stringResource(resId),
                tags = tags,
                onTagClick = onTagClick
            )
        }
    }
}

@Composable
private fun TagGroup(
    label: String,
    tags: List<Tag>?,
    onTagClick: (Tag) -> Unit
) {
    if (tags == null) return

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alignByBaseline()
        )

        tags.forEach { tag ->
            Text(
                text = tag.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .alignByBaseline()
                    .clip(CircleShape)
                    .clickable { onTagClick(tag) }
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(vertical = 4.dp, horizontal = 12.dp)
            )
        }
    }
}

@Preview
@Composable
fun DetailContentHeaderPreview() {
    AmaiTheme {
        Surface {
            DetailContentHeader(
                model = sampleModel(),
                onTagClick = {}
            )
        }
    }
}
