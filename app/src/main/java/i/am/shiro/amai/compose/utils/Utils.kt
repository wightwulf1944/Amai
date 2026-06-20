package i.am.shiro.amai.compose.utils

import androidx.compose.animation.core.spring
import androidx.compose.foundation.pager.PagerState

suspend fun PagerState.animateScrollPageBy(value: Int) {
    val targetPage = currentPage + value
    if (targetPage in 0 until pageCount) {
        animateScrollToPage(
            page = targetPage,
            animationSpec = spring(stiffness = 3000f)
        )
    }
}
