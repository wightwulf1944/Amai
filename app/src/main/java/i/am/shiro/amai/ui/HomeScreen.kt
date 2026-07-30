package i.am.shiro.amai.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import i.am.shiro.amai.R
import i.am.shiro.amai.ui.utils.SharedElementToken

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onSearchClick: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    searchPillToken: SharedElementToken,
) {
    val type = NavigationSuiteScaffoldDefaults.navigationSuiteType(currentWindowAdaptiveInfo())
        .run {
            if (this == NavigationSuiteType.ShortNavigationBarCompact)
                NavigationSuiteType.ShortNavigationBarMedium
            else
                this
        }

    NavigationSuiteScaffold(
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Horizontal)
        ),
        navigationSuiteType = type,
        navigationItems = {
            HomeNavItem(
                selected = state.selectedTab == HomeScreenTab.FAVORITES,
                onClick = { state.selectedTab = HomeScreenTab.FAVORITES },
                iconDrawableRes = R.drawable.ic_favorite,
                labelStringRes = R.string.favorites,
                navigationSuiteType = type,
            )
            HomeNavItem(
                selected = state.selectedTab == HomeScreenTab.NHENTAI,
                onClick = { state.selectedTab = HomeScreenTab.NHENTAI },
                iconDrawableRes = R.drawable.ic_nhentai,
                labelStringRes = R.string.nhentai,
                navigationSuiteType = type,
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
                    NhentaiScreen(
                        backStack = state.nhentaiNavStack,
                        entryDecorators = listOf(holderDecorator, vmStoreDecorator),
                        onSearchClick = onSearchClick,
                        onItemClick = onItemClick,
                        searchPillToken = searchPillToken,
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
    @StringRes labelStringRes: Int,
    navigationSuiteType: NavigationSuiteType,
) {
    val label = stringResource(labelStringRes)
    NavigationSuiteItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(painterResource(iconDrawableRes), label) },
        label = { Text(label) },
        navigationSuiteType = navigationSuiteType,
    )
}
