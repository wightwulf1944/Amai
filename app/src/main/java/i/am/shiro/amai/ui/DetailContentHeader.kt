package i.am.shiro.amai.ui

import android.text.format.DateUtils.getRelativeTimeSpanString
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        Text(
            text = model.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = pluralStringResource(
                    R.plurals.pages_format,
                    model.pageCount,
                    model.pageCount
                ),
                style = MaterialTheme.typography.bodySmall,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (model.uploadDate != null) {
                Text(
                    text = stringResource(
                        R.string.uploaded_format,
                        getRelativeTimeSpanString(model.uploadDate * 1000L)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
}

@Composable
private fun TagGroup(
    label: String,
    tags: List<Tag>?,
    onTagClick: (Tag) -> Unit
) {
    if (tags == null) return

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alignByBaseline()
        )

        for (tag in tags) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.alignByBaseline()
            ) {
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clickable { onTagClick(tag) }
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                )
            }
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
