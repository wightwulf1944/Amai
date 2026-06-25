package i.am.shiro.amai.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import i.am.shiro.amai.coil3.ThumbnailCoilModel

@Composable
fun BookThumbnail(
    url: String,
    aspectRatio: Float,
    modifier: Modifier = Modifier
) = AsyncImage(
    model = ThumbnailCoilModel(url),
    contentDescription = null,
    modifier = modifier
        .aspectRatio(aspectRatio)
        .background(MaterialTheme.colorScheme.surfaceBright)
)
