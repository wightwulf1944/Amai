package i.am.shiro.amai.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import i.am.shiro.amai.R

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
) {
    NavigationSuiteScaffold(
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
                    NavDisplay(
                        backStack = state.nhentaiNavStack,
                        entryDecorators = listOf(holderDecorator, vmStoreDecorator),
                        entryProvider = entryProvider {
                            entry<NhentaiRoute.Latest> {
                                NhentaiLatestGallery(
                                    onSearchClick = onSearchClick,
                                    onItemClick = onItemClick
                                )
                            }
                            entry<NhentaiRoute.Tag> {
                                NhentaiTagGallery(
                                    tagId = it.tagId,
                                    tagName = it.tagName,
                                    onSearchClick = onSearchClick,
                                    onItemClick = onItemClick
                                )
                            }
                            entry<NhentaiRoute.Search> {
                                NhentaiSearchGallery(
                                    searchQuery = it.query,
                                    onSearchClick = onSearchClick,
                                    onItemClick = onItemClick
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
