package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import i.am.shiro.amai.R
import i.am.shiro.amai.model.BookPreview

@Composable
fun BookCard(
    book: BookPreview,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { onItemClick(book.bookId) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        BookThumbnail(
            path = book.thumbnailPath,
            aspectRatio = book.aspectRatio
        )

        Text(
            text = book.title,
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            if (book.showFavoriteBadge) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = pluralStringResource(R.plurals.pages_format, book.pageCount, book.pageCount),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}