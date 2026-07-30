package i.am.shiro.amai.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
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
    val insets = TopAppBarDefaults.windowInsets
        .union(WindowInsets(top = 8.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(background)
            .padding(horizontal = 10.dp)
            .windowInsetsPadding(insets)
            .defaultMinSize(minHeight = 48.dp),
        content = content
    )
}
