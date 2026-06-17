package i.am.shiro.amai.compose

import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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

    val favoritesGridState = rememberLazyStaggeredGridState()
    val browseGridState = rememberLazyStaggeredGridState()

    HomeScaffold(
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it }
    ) {
        when (selectedTab) {
            HomeTab.FAVORITES -> {
                FavoritesScreen(
                    gridState = favoritesGridState,
                    onItemClick = onItemClick
                )
            }

            HomeTab.NHENTAI -> {
                BrowseScreen(
                    gridState = browseGridState,
                    searchEvent = searchEvent,
                    onSearchClick = onSearchClick,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun HomeScaffold(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    content: @Composable () -> Unit,
) {
    NavigationSuiteScaffold(
        navigationItems = {
            NavigationSuiteItem(
                selected = selectedTab == HomeTab.FAVORITES,
                onClick = { onTabSelected(HomeTab.FAVORITES) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorite),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.favorites))
                }
            )
            NavigationSuiteItem(
                selected = selectedTab == HomeTab.NHENTAI,
                onClick = { onTabSelected(HomeTab.NHENTAI) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_nhentai),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.nhentai))
                }
            )
        },
        content = content
    )
}

enum class HomeTab {
    FAVORITES, NHENTAI
}
