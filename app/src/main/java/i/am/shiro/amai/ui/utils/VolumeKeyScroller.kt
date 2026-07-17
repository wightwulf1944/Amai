package i.am.shiro.amai.ui.utils

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
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTime

class VolumeKeyScroller(
    scope: CoroutineScope,
    private val onHoldChange: (Boolean) -> Unit,
    private val onScroll: suspend (delta: Int) -> Unit,
) {
    private val downPressed = MutableStateFlow(false)
    private val upPressed = MutableStateFlow(false)

    init {
        val deltaFlow = combineTransform(downPressed, upPressed) { down, up ->
            when {
                down && !up -> emit(-1)
                up && !down -> emit(1)
            }
        }

        scope.launch {
            while (isActive) {
                delay(300.milliseconds - measureTime {
                    onScroll(deltaFlow.first())
                })
            }
        }

        scope.launch {
            combine(downPressed, upPressed, Boolean::or)
                .distinctUntilChanged()
                .collectLatest { isAnyKeyPressed ->
                    if (isAnyKeyPressed) delay(300.milliseconds)
                    onHoldChange(isAnyKeyPressed)
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
