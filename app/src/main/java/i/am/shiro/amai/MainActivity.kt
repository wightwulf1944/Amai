package i.am.shiro.amai

import android.content.ComponentName
import android.content.Intent
import android.content.Intent.EXTRA_EXCLUDE_COMPONENTS
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import i.am.shiro.amai.compose.DetailScreen
import i.am.shiro.amai.compose.HomeScreen
import i.am.shiro.amai.compose.ReadScreen
import i.am.shiro.amai.compose.SearchScreen
import i.am.shiro.amai.compose.common.AmaiTheme
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.viewmodel.FavoritesViewModel
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.SearchViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data object Search : Route

    @Serializable
    data class Detail(val bookId: Int) : Route

    @Serializable
    data class Read(val bookId: Int, val pageIndex: Int) : Route
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        val initialBookId = when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.data?.pathSegments?.getOrNull(1)?.toIntOrNull()
            }

            Intent.ACTION_SEND -> {
                try {
                    intent.getStringExtra(EXTRA_TEXT)
                        ?.toUri()
                        ?.lastPathSegment
                        ?.toIntOrNull()
                } catch (e: Exception) {
                    Timber.e(e)
                    null
                }
            }

            else -> null
        }

        setContent {
            AmaiTheme {
                val mainViewModel = koinViewModel<MainViewModel>()

                val backStack = rememberNavBackStack(
                    *if (initialBookId == null) {
                        arrayOf(Route.Home)
                    } else {
                        arrayOf(Route.Home, Route.Detail(initialBookId))
                    }
                )

                NavDisplay(
                    backStack = backStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    entryProvider = entryProvider {
                        entry<Route.Home> {
                            HomeScreen(
                                mainViewModel = mainViewModel,
                                nhentaiViewModel = koinViewModel<NhentaiViewModel>(),
                                favoritesViewModel = koinViewModel<FavoritesViewModel>(),
                                onSearchClick = {
                                    backStack += Route.Search
                                },
                                onItemClick = { bookId ->
                                    backStack += Route.Detail(bookId)
                                }
                            )
                        }
                        entry<Route.Search> {
                            val searchViewModel = koinViewModel<SearchViewModel>()
                            val suggestions by searchViewModel.suggestions.collectAsState()
                            SearchScreen(
                                suggestions = suggestions,
                                onQueryChange = { searchViewModel.onQueryChange(it) },
                                onSearch = {
                                    mainViewModel.search(it)
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                        entry<Route.Detail> { key ->
                            DetailScreen(
                                bookId = key.bookId,
                                onBackClick = { backStack.removeLastOrNull() },
                                onShareClick = { share(key.bookId) },
                                onThumbnailClick = { pageIndex ->
                                    backStack += Route.Read(key.bookId, pageIndex)
                                },
                                onTagClick = {
                                    mainViewModel.search(it)
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                        entry<Route.Read> { key ->
                            DisposableEffect(Unit) {
                                val window = window
                                val controller =
                                    WindowInsetsControllerCompat(window, window.decorView)
                                controller.hide(WindowInsetsCompat.Type.statusBars())
                                controller.systemBarsBehavior =
                                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                                onDispose {
                                    controller.show(WindowInsetsCompat.Type.statusBars())
                                }
                            }
                            ReadScreen(
                                bookId = key.bookId,
                                initialPage = key.pageIndex
                            )
                        }
                    }
                )
            }
        }
    }

    private fun share(bookId: Int) {
        val bookUrl = "${Nhentai.WEBPAGE_BASE_URL}${bookId}/"
        val exclude = arrayOf(ComponentName(this, MainActivity::class.java))
        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(EXTRA_TEXT, bookUrl)
            .putExtra(EXTRA_EXCLUDE_COMPONENTS, exclude)
            .setType("text/plain")
            .let { createChooser(it, null) }
        startActivity(intent)
    }
}
