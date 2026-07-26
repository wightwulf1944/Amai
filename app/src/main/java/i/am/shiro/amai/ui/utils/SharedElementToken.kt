package i.am.shiro.amai.ui.utils

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class SharedElementToken(val scope: SharedTransitionScope, val key: Any)

fun SharedTransitionScope.sharedElementToken(key: Any) = SharedElementToken(this, key)

@Composable
fun Modifier.sharedElement(
    token: SharedElementToken,
    animatedVisibilityScope: AnimatedVisibilityScope
) = with(token.scope) {
    val sharedContentState = rememberSharedContentState(token.key)
    sharedElement(sharedContentState, animatedVisibilityScope)
}