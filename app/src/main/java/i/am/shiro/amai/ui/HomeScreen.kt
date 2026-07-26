package i.am.shiro.amai.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import i.am.shiro.amai.R
import i.am.shiro.amai.ui.utils.SharedElementToken

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onSearchClick: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    searchPillToken: SharedElementToken,
) {
    NavigationSuiteScaffold(
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Horizontal)
        ),
        navigationItems = {
            HomeNavItem(
                selected = state.selectedTab == HomeScreenTab.FAVORITES,
                onClick = { state.selectedTab = HomeScreenTab.FAVORITES },
                iconDrawableRes = R.drawable.ic_favorite,
                labelStringRes = R.string.favorites
            )
            HomeNavItem(
                selected = state.selectedTab == HomeScreenTab.NHENTAI,
                onClick = { state.selectedTab = HomeScreenTab.NHENTAI },
                iconDrawableRes = R.drawable.ic_nhentai,
                labelStringRes = R.string.nhentai
            )
        }
    ) {
        val vmStoreDecorator = rememberViewModelStoreNavEntryDecorator<NavKey>()
        val holderDecorator = rememberSaveableStateHolderNavEntryDecorator<NavKey>()
        val saveableStateHolder = rememberSaveableStateHolder()
        saveableStateHolder.SaveableStateProvider(state.selectedTab) {
            when (state.selectedTab) {
                HomeScreenTab.FAVORITES -> {
                    FavoritesScreen(
                        onItemClick = onItemClick
                    )
                }

                HomeScreenTab.NHENTAI -> {
                    val outerNavAnimatedContentScope = LocalNavAnimatedContentScope.current
                    NavDisplay(
                        transitionSpec = { pushTransition },
                        popTransitionSpec = { popTransition },
                        predictivePopTransitionSpec = { popTransition },
                        backStack = state.nhentaiNavStack,
                        entryDecorators = listOf(holderDecorator, vmStoreDecorator),
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
            }
        }
    }
}

@Composable
fun HomeNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes iconDrawableRes: Int,
    @StringRes labelStringRes: Int
) {
    val label = stringResource(labelStringRes)
    NavigationSuiteItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(painterResource(iconDrawableRes), label) },
        label = { Text(label) }
    )
}

private val pushTransition =
    scaleIn(initialScale = 1.05f) + fadeIn() togetherWith scaleOut(targetScale = 0.95f) + fadeOut()

private val popTransition =
    scaleIn(initialScale = 0.95f) + fadeIn() togetherWith scaleOut(targetScale = 1.05f) + fadeOut()
