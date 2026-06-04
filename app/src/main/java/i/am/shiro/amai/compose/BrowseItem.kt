package i.am.shiro.amai.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import i.am.shiro.amai.R
import i.am.shiro.amai.data.view.CachedPreviewView

@Composable
fun BrowseItem(
    book: CachedPreviewView,
    onItemClick: (Int) -> Unit
) {
    Card(
        onClick = { onItemClick(book.bookId) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(book.thumbnailWidth.toFloat() / book.thumbnailHeight.toFloat())
                .background(MaterialTheme.colorScheme.surfaceBright)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.thumbnailUrl)
                    .allowRgb565(true)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

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
            if (book.isSaved) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.pages_format, book.pageCount),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
private fun BrowseItemPreview() {
    val mockBook = CachedPreviewView(
        bookId = 1,
        title = "A Really Long Book Title That Might Take Up Multiple Lines In The Layout",
        pageCount = 123,
        thumbnailWidth = 50,
        thumbnailHeight = 71,
        thumbnailUrl = "",
        isSaved = true
    )
    AmaiTheme {
        BrowseItem(book = mockBook, onItemClick = {})
    }
}