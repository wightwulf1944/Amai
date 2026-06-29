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
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import i.am.shiro.amai.R
import i.am.shiro.amai.ui.viewmodel.MainViewModel
import i.am.shiro.amai.ui.viewmodel.NhentaiRoute

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    mainViewModel: MainViewModel
) {
    NavigationSuiteScaffold(
        navigationItems = {
            HomeNavItem(
                selected = mainViewModel.selectedHomeTab == HomeTab.FAVORITES,
                onClick = { mainViewModel.selectedHomeTab = HomeTab.FAVORITES },
                iconDrawableRes = R.drawable.ic_favorite,
                labelStringRes = R.string.favorites
            )
            HomeNavItem(
                selected = mainViewModel.selectedHomeTab == HomeTab.NHENTAI,
                onClick = { mainViewModel.selectedHomeTab = HomeTab.NHENTAI },
                iconDrawableRes = R.drawable.ic_nhentai,
                labelStringRes = R.string.nhentai
            )
        }
    ) {
        val vmStoreDecorator = rememberViewModelStoreNavEntryDecorator<NhentaiRoute>()
        val holderDecorator = rememberSaveableStateHolderNavEntryDecorator<NhentaiRoute>()
        val saveableStateHolder = rememberSaveableStateHolder()
        saveableStateHolder.SaveableStateProvider(mainViewModel.selectedHomeTab) {
            when (mainViewModel.selectedHomeTab) {
                HomeTab.FAVORITES -> {
                    FavoritesScreen(
                        onItemClick = onItemClick
                    )
                }

                HomeTab.NHENTAI -> {
                    NavDisplay(
                        backStack = mainViewModel.nhentaiNavStack,
                        entryDecorators = listOf(holderDecorator, vmStoreDecorator),
                        entryProvider = entryProvider {
                            entry<NhentaiRoute.Homepage> {
                                HomepageScreen(
                                    onSearchClick = onSearchClick,
                                    onItemClick = onItemClick
                                )
                            }
                            entry<NhentaiRoute.Browse> {
                                BrowseScreen(
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

enum class HomeTab {
    FAVORITES, NHENTAI
}
