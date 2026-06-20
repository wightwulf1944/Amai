package i.am.shiro.amai.compose.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * A [PaddingValues] that takes the maximum values of [first] and [second] for each side.
 */
@Stable
class UnionPaddingValues(
    private val first: PaddingValues,
    private val second: PaddingValues
) : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        maxOf(
            first.calculateLeftPadding(layoutDirection),
            second.calculateLeftPadding(layoutDirection)
        )

    override fun calculateTopPadding(): Dp =
        maxOf(first.calculateTopPadding(), second.calculateTopPadding())

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        maxOf(
            first.calculateRightPadding(layoutDirection),
            second.calculateRightPadding(layoutDirection)
        )

    override fun calculateBottomPadding(): Dp =
        maxOf(first.calculateBottomPadding(), second.calculateBottomPadding())

    override fun hashCode(): Int = first.hashCode() + second.hashCode() * 31

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnionPaddingValues) return false
        return first == other.first && second == other.second
    }

    override fun toString(): String = "Union($first, $second)"
}

/**
 * Returns a [PaddingValues] that represents the maximum of each side between this and [other].
 */
@Stable
infix fun PaddingValues.union(other: PaddingValues): PaddingValues = UnionPaddingValues(this, other)
