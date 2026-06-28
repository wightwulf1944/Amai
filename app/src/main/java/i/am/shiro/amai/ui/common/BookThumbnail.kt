package i.am.shiro.amai.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import i.am.shiro.amai.ui.image.ThumbnailModel

@Composable
fun BookThumbnail(
    path: String,
    aspectRatio: Float,
    modifier: Modifier = Modifier
) = AsyncImage(
    model = ThumbnailModel(path),
    contentDescription = null,
    modifier = modifier
        .aspectRatio(aspectRatio)
        .background(MaterialTheme.colorScheme.surfaceBright)
)
