package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AmaiScaffold(
    snackbarHost: @Composable () -> Unit = {},
    topBar: @Composable RowScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .union(WindowInsets(8.dp, 8.dp, 8.dp, 8.dp)),
        snackbarHost = snackbarHost,
        topBar = {
            TopBarContainer {
                topBar()
            }
        },
        content = content
    )
}