package i.am.shiro.amai.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import i.am.shiro.amai.R
import i.am.shiro.amai.viewmodel.FavoritesViewModel
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    nhentaiViewModel: NhentaiViewModel,
    favoritesViewModel: FavoritesViewModel,
    onSearchClick: () -> Unit,
    onItemClick: (Int) -> Unit,
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

    val snackbarHostState = remember { SnackbarHostState() }

    HomeBackHandler(snackbarHostState)

    Scaffold(
        contentWindowInsets = WindowInsets(),
        bottomBar = {
            HomeBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {

                val favoritesGridState = rememberLazyStaggeredGridState()
                val nhentaiGridState = rememberLazyStaggeredGridState()

                when (selectedTab) {
                    HomeTab.FAVORITES -> {
                        val books by favoritesViewModel.books.collectAsState()
                        var shouldScrollToTop by remember { mutableStateOf(false) }

                        LaunchedEffect(books) {
                            if (shouldScrollToTop) {
                                favoritesGridState.scrollToItem(0)
                                shouldScrollToTop = false
                            }
                        }

                        FavoritesScreen(
                            books = books,
                            onSortChanged = { sort ->
                                shouldScrollToTop = true
                                favoritesViewModel.onSort(sort)
                            },
                            onSearchSubmit = { searchQuery ->
                                shouldScrollToTop = true
                                favoritesViewModel.onSearch(searchQuery)
                            },
                            onItemClick = onItemClick,
                            gridState = favoritesGridState
                        )
                    }

                    HomeTab.NHENTAI -> {
                        val books by nhentaiViewModel.books.collectAsState()
                        val isLoading by nhentaiViewModel.isLoading.collectAsState()

                        val initialTitle = stringResource(R.string.nhentai)
                        var title by remember { mutableStateOf(initialTitle) }
                        var scrollTrigger by remember { mutableIntStateOf(0) }

                        LaunchedEffect(scrollTrigger) {
                            if (scrollTrigger > 0) {
                                nhentaiGridState.scrollToItem(0)
                            }
                        }

                        LaunchedEffect(nhentaiGridState.canScrollForward) {
                            if (!nhentaiGridState.canScrollForward) {
                                nhentaiViewModel.onScrollToBottom()
                            }
                        }

                        LaunchedEffect(searchEvent) {
                            searchEvent?.let { event ->
                                if (!event.isNhentaiConsumed) {
                                    title = event.query
                                    scrollTrigger++
                                    nhentaiViewModel.onSearch(event.query)
                                    event.isNhentaiConsumed = true
                                }
                            }
                        }

                        BrowseScreen(
                            title = title,
                            books = books,
                            isLoading = isLoading,
                            onRefresh = nhentaiViewModel::onRefresh,
                            onSortChanged = nhentaiViewModel::onSort,
                            onSearchClick = onSearchClick,
                            onItemClick = onItemClick,
                            gridState = nhentaiGridState
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun HomeBottomNavigation(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
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
        NavigationBarItem(
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
    }
}

enum class HomeTab {
    FAVORITES, NHENTAI
}
