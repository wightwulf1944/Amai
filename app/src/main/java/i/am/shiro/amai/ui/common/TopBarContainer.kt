package i.am.shiro.amai.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
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
import i.am.shiro.amai.ui.utils.asSymmetricHorizontal

@Composable
fun TopBarContainer(
    content: @Composable RowScope.() -> Unit
) {
    val background = Brush.verticalGradient(
        listOf(MaterialTheme.colorScheme.surface, Color.Transparent)
    )
    val insets = TopAppBarDefaults.windowInsets
        .asSymmetricHorizontal(0.5f)
        .add(WindowInsets(left = 16.dp, right = 16.dp, top = 4.dp))
        .union(WindowInsets(top = 6.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(background)
            .windowInsetsPadding(insets),
        content = content
    )
}
