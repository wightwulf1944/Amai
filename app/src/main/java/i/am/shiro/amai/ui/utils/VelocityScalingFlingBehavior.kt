package i.am.shiro.amai.ui.utils

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * A [FlingBehavior] decorator that scales the initial velocity of a fling.
 *
 * @param delegate The base [FlingBehavior] to decorate.
 * @param scaleFactor The multiplier to apply to the initial fling velocity.
 */
class VelocityScalingFlingBehavior(
    private val delegate: FlingBehavior,
    private val scaleFactor: Float,
) : FlingBehavior {
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        return with(delegate) {
            performFling(initialVelocity * scaleFactor)
        }
    }
}

@Composable
fun rememberVelocityScalingFlingBehavior(scaleFactor: Float): FlingBehavior {
    val defaultFling = ScrollableDefaults.flingBehavior()
    return remember(defaultFling) {
        VelocityScalingFlingBehavior(defaultFling, scaleFactor)
    }
}