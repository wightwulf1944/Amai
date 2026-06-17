package i.am.shiro.amai.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowRgb565
import coil3.size.Precision
import i.am.shiro.amai.compose.common.AmaiTheme
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.viewmodel.ReadViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReadScreen(
    bookId: Int,
    initialPage: Int,
    viewModel: ReadViewModel = koinViewModel()
) {
    LaunchedEffect(bookId) {
        viewModel.setBookId(bookId)
    }

    val pages by viewModel.pages.collectAsState()

    ReadContent(
        pages = pages,
        initialPage = initialPage
    )
}

@Composable
fun ReadContent(
    pages: List<ImageEntity>,
    initialPage: Int
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { pages.size }
    val focusRequester = remember { FocusRequester() }

    val scope = rememberCoroutineScope()
    val volumeDownHandler = remember { KeyEventHandler(scope) }
    val volumeUpHandler = remember { KeyEventHandler(scope) }

    var turboOn by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                when (keyEvent.key) {
                    Key.VolumeDown -> volumeDownHandler.handleKeyEvent(
                        keyEvent = keyEvent,
                        onAction = {
                            if (pagerState.currentPage > 0) {
                                pagerState.animateScrollPageBy(-1)
                            }
                        },
                        onHoldChanged = { turboOn = it }
                    )

                    Key.VolumeUp -> volumeUpHandler.handleKeyEvent(
                        keyEvent = keyEvent,
                        onAction = {
                            if (pagerState.currentPage < pagerState.pageCount - 1) {
                                pagerState.animateScrollPageBy(1)
                            }
                        },
                        onHoldChanged = { turboOn = it }
                    )

                    else -> false
                }
            }
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
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
            )

            // TODO show some visual indicator of reading progress like a bar or scrollthumb
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
        targetValue = if (turboOn) 0.9f else 1f,
        label = "pageScale"
    )
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        key = { index -> if (index < pages.size) "${pages[index].bookId}_${pages[index].pageIndex}" else index },
    ) { index ->
        val page = pages[index]
        val context = LocalContext.current
        val sizeResolver = rememberConstraintsSizeResolver()

        val thumbnailPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(page.thumbnailUrl)
                .size(sizeResolver)
                .allowRgb565(true)
                .precision(Precision.INEXACT)
                .build(),
            filterQuality = FilterQuality.None
        )

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(if (turboOn) null else page.url)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build(),
            placeholder = thumbnailPainter,
            fallback = thumbnailPainter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .then(sizeResolver)
        )
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
