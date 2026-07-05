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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration

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

fun String.tokenize(): AnnotatedString {
    var searchMode = SearchMode.START
    var startI = -1

    val spans = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    fun addSpan(start: Int, end: Int) {
        spans += AnnotatedString.Range(
            SpanStyle(textDecoration = TextDecoration.Underline),
            start,
            end
        )
    }

    forEachIndexed { i, c ->
        when (searchMode) {
            SearchMode.START -> {
                when (c) {
                    '"' -> {
                        startI = i
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        // unexpected, do nothing
                    }

                    ' ' -> {
                        // stay in START
                    }

                    else -> {
                        startI = i
                        searchMode = SearchMode.END
                    }
                }
            }

            SearchMode.CONTINUE -> {
                when (c) {
                    '"' -> {
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        // unexpected, do nothing
                    }

                    ' ' -> {
                        // unexpected, end current token
                        addSpan(startI, i)
                        searchMode = SearchMode.START
                    }

                    else -> {
                        searchMode = SearchMode.END
                    }
                }
            }

            SearchMode.END -> {
                when (c) {
                    '"' -> {
                        // unexpected, end current token and start new token
                        addSpan(startI, i)
                        startI = i
                        searchMode = SearchMode.END_QUOTE
                    }

                    ':' -> {
                        searchMode = SearchMode.CONTINUE
                    }

                    ' ' -> {
                        addSpan(startI, i)
                        searchMode = SearchMode.START
                    }

                    else -> {
                        // stay in END
                    }
                }
            }

            SearchMode.END_QUOTE -> {
                when (c) {
                    '"' -> {
                        addSpan(startI, i + 1) // i + 1 means include current char in span
                        searchMode = SearchMode.START
                    }

                    else -> {
                        // inside quotes: do nothing
                    }
                }
            }
        }
    }
    if (searchMode != SearchMode.START) {
        addSpan(startI, length)
    }

    return AnnotatedString(
        text = this,
        spanStyles = spans
    )
}

private enum class SearchMode {
    START, CONTINUE, END, END_QUOTE
}

@Suppress("FunctionName")
fun HapticFeedbackType.Companion.Toggle(on: Boolean): HapticFeedbackType {
    return if (on) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
}