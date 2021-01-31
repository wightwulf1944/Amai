package i.am.shiro.amai.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import timber.log.Timber

class PullGestureLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var childOffset: Float = 0.0f

    private val maxOffset = 72 * resources.displayMetrics.density

    private var onPullListener = {}

    fun setOnPullListener(onPullListener: () -> Unit) {
        this.onPullListener = onPullListener
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes or ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onStopNestedScroll(child: View) {
        super.onStopNestedScroll(child)
        if (childOffset == maxOffset) onPullListener()
        childOffset = 0.0f
        child.animate()
            .setDuration(100)
            .translationY(childOffset)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (childOffset > 0 && dy > 0) {
            childOffset = (childOffset - dy).coerceAtLeast(0.0f)
            target.translationY = childOffset
            consumed[1] = dy
            Timber.d("RABBIT $childOffset | $dy")
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed)
        }
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        if (childOffset < maxOffset && dyUnconsumed < 0) {
            childOffset = (childOffset - dyUnconsumed).coerceAtMost(maxOffset)
            target.translationY = childOffset
            Timber.d("RABBIT $childOffset | $dyUnconsumed ")
        } else {
            super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        }
    }
}