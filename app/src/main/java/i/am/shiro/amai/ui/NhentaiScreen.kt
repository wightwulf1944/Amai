package i.am.shiro.amai.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import i.am.shiro.amai.ui.utils.SharedElementToken

@Composable
fun NhentaiScreen(
    backStack: NavBackStack<NavKey>,
    entryDecorators: List<NavEntryDecorator<NavKey>>,
    onSearchClick: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    searchPillToken: SharedElementToken,
) {
    val outerNavAnimatedContentScope = LocalNavAnimatedContentScope.current
    NavDisplay(
        transitionSpec = { pushTransition },
        popTransitionSpec = { popTransition },
        predictivePopTransitionSpec = { popTransition },
        backStack = backStack,
        entryDecorators = entryDecorators,
        entryProvider = entryProvider {
            entry<NhentaiRoute.Latest> {
                NhentaiLatestGallery(
                    onSearchClick = { onSearchClick("") },
                    onItemClick = onItemClick,
                    searchPillToken = searchPillToken,
                    navAnimatedContentScope = outerNavAnimatedContentScope,
                )
            }
            entry<NhentaiRoute.Tag> {
                NhentaiTagGallery(
                    tagId = it.id,
                    tagName = it.name,
                    onSearchClick = { onSearchClick(it.query) },
                    onItemClick = onItemClick,
                    searchPillToken = searchPillToken,
                    navAnimatedContentScope = outerNavAnimatedContentScope,
                )
            }
            entry<NhentaiRoute.Search> {
                NhentaiSearchGallery(
                    searchQuery = it.query,
                    onSearchClick = { onSearchClick(it.query) },
                    onItemClick = onItemClick,
                    searchPillToken = searchPillToken,
                    navAnimatedContentScope = outerNavAnimatedContentScope,
                )
            }
        }
    )
}

private val pushTransition =
    scaleIn(initialScale = 1.05f) + fadeIn() togetherWith scaleOut(targetScale = 0.95f) + fadeOut()

private val popTransition =
    scaleIn(initialScale = 0.95f) + fadeIn() togetherWith scaleOut(targetScale = 1.05f) + fadeOut()