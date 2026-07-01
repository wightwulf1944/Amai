package i.am.shiro.amai.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import i.am.shiro.amai.ui.utils.asSymmetricHorizontal

@Composable
fun AmaiScaffold(
    snackbarHost: @Composable () -> Unit = {},
    topBar: @Composable RowScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.asSymmetricHorizontal(0.5f),
        snackbarHost = snackbarHost,
        topBar = {
            TopBarContainer {
                topBar()
            }
        },
        content = content
    )
}