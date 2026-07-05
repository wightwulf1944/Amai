package i.am.shiro.amai.ui.utils

import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalLayoutDirection

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
 * Logs the values of [PaddingValues] to the provided [logger] during composition
 * and whenever any of the padding values change.
 */
@Composable
fun PaddingValues.debug(logger: (String) -> Unit): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    val start = calculateStartPadding(layoutDirection)
    val top = calculateTopPadding()
    val end = calculateEndPadding(layoutDirection)
    val bottom = calculateBottomPadding()

    LaunchedEffect(start, top, end, bottom) {
        logger("PaddingValues(start=$start, top=$top, end=$end, bottom=$bottom)")
    }

    return this
}

@Suppress("FunctionName")
fun HapticFeedbackType.Companion.Toggle(on: Boolean): HapticFeedbackType {
    return if (on) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
}