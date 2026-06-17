package i.am.shiro.amai.compose.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * A type-safe, encapsulated wrapper around [NavBackStack] that enforces guarded 
 * navigation operations.
 *
 * By delegating to [List], this class provides read-only access to the backstack 
 * while funneling all mutations through specific, safety-checked operations. This 
 * protects the navigation state from common UI-driven issues like rapid taps or
 * unauthorized mutations from outside navigation logic.
 *
 * **Usage Note**: Because this class implements [List] but not `MutableList`, 
 * `NavDisplay` cannot automatically handle back-navigation. When using this 
 * class, you must explicitly pass [popUnsafe] to the `onBack` parameter of 
 * your `NavDisplay`.
 *
 * @param T The type of [NavKey] used for navigation routes.
 */
@Stable
class Navigator<T : NavKey>(
    private val backStack: NavBackStack<T>
) : List<T> by backStack {

    /**
     * Pushes a new [key] onto the stack.
     *
     * If the [key] is already at the top of the stack, this operation is a no-op. This
     * prevents duplicate destinations that can be caused by rapid UI interactions,
     * such as a user tapping a button multiple times in quick succession.
     */
    fun push(key: T) {
        if (lastOrNull() != key) backStack.add(key)
    }

    /**
     * Pops the top item off the stack if it matches the provided [key].
     *
     * If the provided [key] is not at the top of the stack, this operation is a no-op.
     * This ensures that a "back" action only removes the specific screen the developer
     * intended to close, protecting against accidental "multi-pops" caused by rapid
     * UI interactions.
     */
    fun pop(key: T) {
        if (lastOrNull() == key) backStack.removeLastOrNull()
    }

    /**
     * Pops the top item off the stack without a key check.
     *
     * This is intended for `NavDisplay.onBack` because the framework handles the
     * necessary safety and state validation in that context. Using this function 
     * in response to manual UI clicks is discouraged as it lacks the rapid-tap 
     * guards provided by [pop].
     */
    fun popUnsafe() {
        backStack.removeLastOrNull()
    }
}

/**
 * Creates and remembers a [Navigator] for the given [initial] keys.
 *
 * @param initial The initial set of keys to populate the backstack.
 */
@Composable
fun <T : NavKey> rememberNavigator(vararg initial: T): Navigator<T> {
    @Suppress("UNCHECKED_CAST")
    val backStack = rememberNavBackStack(*initial) as NavBackStack<T>
    return remember(backStack) { Navigator(backStack) }
}
