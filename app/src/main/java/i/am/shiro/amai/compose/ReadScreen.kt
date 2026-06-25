package i.am.shiro.amai.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import i.am.shiro.amai.coil3.PageCoilModel
import i.am.shiro.amai.coil3.ThumbnailCoilModel
import i.am.shiro.amai.compose.common.AmaiTheme
import i.am.shiro.amai.compose.utils.VolumeKeyHandler
import i.am.shiro.amai.compose.utils.animateScrollPageBy
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.viewmodel.ReadViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReadScreen(
    bookId: Int,
    initialPage: Int,
    viewModel: ReadViewModel = koinViewModel {
        parametersOf(bookId)
    }
) {
    ReadContent(
        initialPage = initialPage,
        pages = viewModel.pages
    )
}

@Composable
fun ReadContent(
    initialPage: Int,
    pages: List<ImageEntity>
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { pages.size }
    val focusRequester = remember { FocusRequester() }
    var turboOn by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val handler = remember {
        VolumeKeyHandler(
            scope = scope,
            onHoldChanged = { turboOn = it },
            onVolumeDown = {
                pagerState.animateScrollPageBy(-1)
            },
            onVolumeUp = {
                pagerState.animateScrollPageBy(1)
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent(handler::handleKeyEvent)
    ) {
        if (pages.isNotEmpty()) {
            ReadPager(
                pages = pages,
                pagerState = pagerState,
                turboOn = turboOn
            )

            PageCounter(
                currentPage = pagerState.currentPage + 1,
                pageCount = pages.size,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .safeDrawingPadding()
                    .padding(bottom = 16.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun ReadPager(
    pages: List<ImageEntity>,
    pagerState: PagerState,
    turboOn: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (turboOn) 0.85f else 1f,
        label = "pageScale"
    )
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        key = { index -> if (index < pages.size) "${pages[index].bookId}_${pages[index].pageIndex}" else index },
    ) { index ->
        val page = pages[index]

        if (turboOn) {
            AsyncImage(
                model = ThumbnailCoilModel(page.thumbnailPath),
                filterQuality = FilterQuality.None,
                contentDescription = null,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .aspectRatio(page.thumbnailWidth.toFloat() / page.thumbnailHeight.toFloat())
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                placeholder = ColorPainter(Color.Gray)
            )
        } else {
            SubcomposeAsyncImage(
                model = PageCoilModel(page.path),
                filterQuality = FilterQuality.High,
                contentDescription = null,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .aspectRatio(page.width.toFloat() / page.height.toFloat())
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                loading = {
                    AsyncImage(
                        model = ThumbnailCoilModel(page.thumbnailPath),
                        filterQuality = FilterQuality.None,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(page.thumbnailWidth.toFloat() / page.thumbnailHeight.toFloat()),
                        placeholder = ColorPainter(Color.Gray)
                    )
                }
            )
        }
    }
}

@Composable
private fun PageCounter(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier
) {
    Text(
        text = "$currentPage/$pageCount",
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun PageCounterPreview() {
    AmaiTheme {
        PageCounter(
            currentPage = 5,
            pageCount = 42,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview
@Composable
private fun ReadContentPreview() {
    AmaiTheme {
        ReadContent(
            pages = listOf(
                ImageEntity(0, 0, 0, 0, "", 0, 0, ""),
                ImageEntity(0, 1, 0, 0, "", 0, 0, "")
            ),
            initialPage = 0
        )
    }
}
