package i.am.shiro.amai.compose

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource.Monotonic.markNow

@Composable
fun HomeBackHandler(
    snackbarHostState: SnackbarHostState
) {
    var lastBackPressTime by remember { mutableStateOf(markNow()) }
    val scope = rememberCoroutineScope()
    val confirmExitText = stringResource(R.string.confirm_exit)
    val activity = LocalActivity.current

    BackHandler {
        if (lastBackPressTime.elapsedNow() > 1.seconds) {
            lastBackPressTime = markNow()
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(confirmExitText)
            }
        } else {
            activity?.finish()
        }
    }
}