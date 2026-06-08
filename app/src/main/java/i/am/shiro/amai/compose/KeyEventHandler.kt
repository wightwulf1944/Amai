package i.am.shiro.amai.compose

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * Utility to handle KeyEvents by throttling actions in a single worker coroutine
 */
class KeyEventHandler(scope: CoroutineScope) {

    private val actionQueue = Channel<suspend () -> Unit>()

    init {
        scope.launch {
            for (action in actionQueue) {
                action()
            }
        }
    }

    fun handleKeyEvent(
        keyEvent: KeyEvent,
        onAction: suspend () -> Unit,
        onHoldChanged: (Boolean) -> Unit,
    ): Boolean {
        when (keyEvent.type) {
            KeyEventType.KeyDown -> {

                actionQueue.trySend { onAction() }

                if (keyEvent.nativeKeyEvent.repeatCount > 0) {
                    onHoldChanged(true)
                }
            }

            KeyEventType.KeyUp -> {
                onHoldChanged(false)
            }
        }
        return true
    }
}
