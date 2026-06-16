package i.am.shiro.amai.compose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopBarContainer(
    content: @Composable RowScope.() -> Unit
) {
    val background = Brush.verticalGradient(
        listOf(MaterialTheme.colorScheme.surface, Color.Transparent)
    )
    val insets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .windowInsetsPadding(insets)
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp),
        content = content
    )
}
