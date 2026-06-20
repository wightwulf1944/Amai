package i.am.shiro.amai.compose.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.lerp

/**
 * A [WindowInsets] that makes the horizontal values of [wrapped] symmetric
 * by taking the maximum of left and right, while preserving vertical values.
 */
@Stable
class SymmetricHorizontalInsets(
    private val wrapped: WindowInsets,
    private val bias: Float
) : WindowInsets {
    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int {
        val left = wrapped.getLeft(density, layoutDirection)
        val right = wrapped.getRight(density, layoutDirection)
        return lerp(left, maxOf(left, right), bias)
    }

    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int {
        val left = wrapped.getLeft(density, layoutDirection)
        val right = wrapped.getRight(density, layoutDirection)
        return lerp(right, maxOf(left, right), bias)
    }

    override fun getTop(density: Density): Int = wrapped.getTop(density)

    override fun getBottom(density: Density): Int = wrapped.getBottom(density)

    override fun hashCode(): Int = wrapped.hashCode() * 31 + bias.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SymmetricHorizontalInsets) return false
        return wrapped == other.wrapped && bias == other.bias
    }

    override fun toString(): String = "SymmetricHorizontal(wrapped=$wrapped, bias=$bias)"
}

/**
 * Returns a [WindowInsets] that is horizontally symmetric by taking the maximum of
 * the left and right values, while preserving top and bottom.
 */
fun WindowInsets.asSymmetricHorizontal(bias: Float = 1f): WindowInsets = SymmetricHorizontalInsets(this, bias)
