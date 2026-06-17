package i.am.shiro.amai.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.NHENTAI) }

    val searchEvent by mainViewModel.searchEvent.collectAsState(null)
    LaunchedEffect(searchEvent) {
        searchEvent?.let { event ->
            if (!event.isHomeConsumed) {
                selectedTab = HomeTab.NHENTAI
                event.isHomeConsumed = true
            }
        }
    }

    HomeScaffold(
        selectedTab = selectedTab,
        onTabSelected = { it: HomeTab -> selectedTab = it }
    ) {
        when (selectedTab) {
            HomeTab.FAVORITES -> {
                FavoritesScreen(
                    onItemClick = onItemClick
                )
            }

            HomeTab.NHENTAI -> {
                BrowseScreen(
                    mainViewModel = mainViewModel,
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
