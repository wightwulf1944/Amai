package i.am.shiro.amai.compose

import androidx.compose.animation.core.spring
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import coil.size.Dimension
import coil.size.Size
import coil.size.SizeResolver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

suspend fun PagerState.animateScrollPageBy(value: Int) = animateScrollToPage(
    page = currentPage + value,
    animationSpec = spring(stiffness = 3000f)
)

class ConstraintsSizeResolver : SizeResolver, LayoutModifier {

    private val currentConstraints = MutableStateFlow(Constraints.fixed(0, 0))

    override suspend fun size(): Size {
        return currentConstraints
            .mapNotNull(Constraints::toSizeOrNull)
            .first()
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        // Cache the current constraints.
        currentConstraints.value = constraints

        // Measure and layout the content.
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

fun Constraints.toSizeOrNull(): Size? {
    if (isZero) {
        return null
    } else {
        val width = if (hasBoundedWidth) Dimension(maxWidth) else Dimension.Undefined
        val height = if (hasBoundedHeight) Dimension(maxHeight) else Dimension.Undefined
        return Size(width, height)
    }
}