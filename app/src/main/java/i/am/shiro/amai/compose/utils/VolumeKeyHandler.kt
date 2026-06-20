package i.am.shiro.amai.compose.utils

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTime

class VolumeKeyHandler(
    scope: CoroutineScope,
    private val onHoldChanged: (Boolean) -> Unit,
    private val onVolumeDown: suspend () -> Unit,
    private val onVolumeUp: suspend () -> Unit
) {
    private val downPressed = MutableStateFlow(false)
    private val upPressed = MutableStateFlow(false)

    init {
        val actionFlow = combine(downPressed, upPressed) { down, up ->
            when {
                down && !up -> onVolumeDown
                up && !down -> onVolumeUp
                else -> null
            }
        }

        scope.launch {
            while (isActive) {
                val action = actionFlow.filterNotNull().first()
                val elapsed = measureTime {
                    action()
                }
                delay(300.milliseconds - elapsed)
            }
        }

        val isAnyPressedFlow = combine(downPressed, upPressed) { down, up -> down || up }
            .distinctUntilChanged()

        scope.launch {
            isAnyPressedFlow.collectLatest { isAnyPressed ->
                if (isAnyPressed) {
                    delay(300.milliseconds)
                    onHoldChanged(true)
                } else {
                    onHoldChanged(false)
                }
            }
        }
    }

    fun handleKeyEvent(event: KeyEvent): Boolean {
        val isDown = event.type == KeyEventType.KeyDown
        return when (event.key) {
            Key.VolumeUp -> {
                upPressed.value = isDown
                true
            }

            Key.VolumeDown -> {
                downPressed.value = isDown
                true
            }

            else -> false
        }
    }
}
