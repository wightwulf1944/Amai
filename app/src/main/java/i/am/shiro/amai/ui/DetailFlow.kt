package i.am.shiro.amai.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import i.am.shiro.amai.model.Tag
import i.am.shiro.amai.ui.navigation.rememberNavigator
import kotlinx.serialization.Serializable

@Composable
fun DetailFlow(
    bookId: Int,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onTagClick: (Tag) -> Unit
) {
    val navigator = rememberNavigator<DetailFlowDestination>(DetailFlowDestination.Detail)
    var highlightedPageIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    NavDisplay(
        backStack = navigator,
        onBack = navigator::popUnsafe,
        predictivePopTransitionSpec = { fadeIn() togetherWith fadeOut() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<DetailFlowDestination.Detail> {
                DetailScreen(
                    bookId = bookId,
                    highlightedPageIndex = highlightedPageIndex,
                    onBackClick = onBackClick,
                    onShareClick = onShareClick,
                    onReadPage = { initialPage ->
                        navigator.push(DetailFlowDestination.Read(initialPage))
                    },
                    onTagClick = onTagClick
                )
            }
            entry<DetailFlowDestination.Read> { key ->
                ReadScreen(
                    bookId = bookId,
                    initialPage = key.initialPage,
                    onClose = { settledPage ->
                        highlightedPageIndex = settledPage
                        navigator.pop(key)
                    }
                )
            }
        }
    )
}

@Serializable
private sealed interface DetailFlowDestination : NavKey {

    @Serializable
    data object Detail : DetailFlowDestination

    @Serializable
    data class Read(val initialPage: Int) : DetailFlowDestination
}
