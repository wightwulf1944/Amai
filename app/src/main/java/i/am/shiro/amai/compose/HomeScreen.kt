package i.am.shiro.amai.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.model.SearchEvent

@Composable
fun HomeScreen(
    searchEvent: SearchEvent?,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.NHENTAI) }

    LaunchedEffect(searchEvent) {
        if (searchEvent?.isProcessing == true) {
            selectedTab = HomeTab.NHENTAI
        }
    }

    NavigationSuiteScaffold(
        navigationItems = {
            HomeNavItem(
                selected = selectedTab == HomeTab.FAVORITES,
                onClick = { selectedTab = HomeTab.FAVORITES },
                iconDrawableRes = R.drawable.ic_favorite,
                labelStringRes = R.string.favorites
            )
            HomeNavItem(
                selected = selectedTab == HomeTab.NHENTAI,
                onClick = { selectedTab = HomeTab.NHENTAI },
                iconDrawableRes = R.drawable.ic_nhentai,
                labelStringRes = R.string.nhentai
            )
        }
    ) {
        val saveableStateHolder = rememberSaveableStateHolder()
        saveableStateHolder.SaveableStateProvider(selectedTab) {
            when (selectedTab) {
                HomeTab.FAVORITES -> {
                    FavoritesScreen(
                        onItemClick = onItemClick
                    )
                }

                HomeTab.NHENTAI -> {
                    BrowseScreen(
                        searchEvent = searchEvent,
                        onSearchClick = onSearchClick,
                        onItemClick = onItemClick
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
