package i.am.shiro.amai.ui.utils

import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.unit.LayoutDirection

suspend fun PagerState.animateScrollPageBy(value: Int) {
    val targetPage = currentPage + value
    if (targetPage in 0 until pageCount) {
        animateScrollToPage(
            page = targetPage,
            animationSpec = spring(stiffness = 3000f)
        )
    }
}

/**
 * Returns a formatted string of the [PaddingValues] for the given [layoutDirection].
 * Useful for logging and debugging.
 */
fun PaddingValues.format(layoutDirection: LayoutDirection): String {
    val start = calculateStartPadding(layoutDirection)
    val top = calculateTopPadding()
    val end = calculateEndPadding(layoutDirection)
    val bottom = calculateBottomPadding()
    return "PaddingValues(start=$start, top=$top, end=$end, bottom=$bottom)"
}
