package i.am.shiro.amai.compose

import androidx.compose.animation.core.spring
import androidx.compose.foundation.pager.PagerState

suspend fun PagerState.animateScrollPageBy(value: Int) = animateScrollToPage(
    page = currentPage + value,
    animationSpec = spring(stiffness = 3000f)
)
