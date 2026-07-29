package i.am.shiro.amai.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import i.am.shiro.amai.ui.utils.SharedElementToken
import i.am.shiro.amai.ui.utils.sharedElement

@Composable
fun NhentaiScreen(
    backStack: NavBackStack<NavKey>,
    entryDecorators: List<NavEntryDecorator<NavKey>>,
    onSearchClick: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    searchPillToken: SharedElementToken,
) {
    val sharedElementMod =
        Modifier.sharedElement(searchPillToken, LocalNavAnimatedContentScope.current)

    NavDisplay(
        predictivePopTransitionSpec = { fadeIn() togetherWith fadeOut() },
        backStack = backStack,
        entryDecorators = entryDecorators,
        entryProvider = entryProvider {
            entry<NhentaiRoute.Latest> {
                NhentaiLatestGallery(
                    onSearchClick = { onSearchClick("") },
                    onItemClick = onItemClick,
                    sharedElementModifier = sharedElementMod,
                )
            }
            entry<NhentaiRoute.Tag> {
                NhentaiTagGallery(
                    tagId = it.id,
                    tagName = it.name,
                    onSearchClick = { onSearchClick(it.query) },
                    onItemClick = onItemClick,
                    sharedElementModifier = sharedElementMod,
                )
            }
            entry<NhentaiRoute.Search> {
                NhentaiSearchGallery(
                    searchQuery = it.query,
                    onSearchClick = { onSearchClick(it.query) },
                    onItemClick = onItemClick,
                    sharedElementModifier = sharedElementMod,
                )
            }
        }
    )
}
