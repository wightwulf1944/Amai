package i.am.shiro.amai.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import timber.log.Timber

class PullGestureBehavior(context: Context, attrs: AttributeSet)
    : CoordinatorLayout.Behavior<View>(context, attrs) {

    private val maxOffset = context.resources.displayMetrics.density * 72

    private var childOffset = 0.0f

    private var onPullListener = {}

    fun setOnPullListener(onPullListener: () -> Unit) {
        this.onPullListener = onPullListener
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes or ViewCompat.SCROLL_AXIS_VERTICAL != 0 && type == ViewCompat.TYPE_TOUCH
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        if (childOffset == maxOffset) onPullListener()
        childOffset = 0.0f
        child.animate()
            .setDuration(100)
            .translationY(childOffset)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (childOffset > 0 && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
            childOffset = (childOffset - dy).coerceAtLeast(0.0f)
            target.translationY = childOffset
            target.invalidate()
            consumed[1] = dy
            Timber.d("RABBIT $childOffset | $dy")
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
        if (childOffset < maxOffset && dyUnconsumed < 0 && type == ViewCompat.TYPE_TOUCH) {
            childOffset = (childOffset - dyUnconsumed).coerceAtMost(maxOffset)
            target.animate()
                .setDuration(0)
                .translationY(childOffset)
            target.translationY = childOffset
            target.invalidate()
//            target.postInvalidate()
            consumed[1] = dyUnconsumed
            Timber.d("RABBIT $childOffset | $dyUnconsumed ")
        } else {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        }
    }
}