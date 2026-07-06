package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun AmaiScaffold(
    snackbarHost: @Composable () -> Unit = {},
    topBar: @Composable RowScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            TopBarContainer {
                topBar()
            }
        },
        content = content
    )
}