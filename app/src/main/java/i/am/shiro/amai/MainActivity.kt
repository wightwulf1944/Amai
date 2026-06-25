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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import i.am.shiro.amai.compose.DetailScreen
import i.am.shiro.amai.compose.HomeScreen
import i.am.shiro.amai.compose.ReadScreen
import i.am.shiro.amai.compose.SearchScreen
import i.am.shiro.amai.compose.common.AmaiTheme
import i.am.shiro.amai.compose.common.rememberNavigator
import i.am.shiro.amai.model.SearchEvent
import i.am.shiro.amai.network.Nhentai
import kotlinx.serialization.Serializable
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
                val shouldShowStatusBars = currentWindowAdaptiveInfo().windowSizeClass
                    .isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)

                DisposableEffect(shouldShowStatusBars) {
                    val controller = WindowInsetsControllerCompat(window, window.decorView)
                    if (shouldShowStatusBars) {
                        controller.show(WindowInsetsCompat.Type.statusBars())
                        controller.systemBarsBehavior = BEHAVIOR_DEFAULT

                    } else {
                        controller.hide(WindowInsetsCompat.Type.statusBars())
                        controller.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                    onDispose {
                        controller.show(WindowInsetsCompat.Type.statusBars())
                        controller.systemBarsBehavior = BEHAVIOR_DEFAULT
                    }
                }

                var searchEvent by rememberSaveable { mutableStateOf<SearchEvent?>(null) }

                val navigator = rememberNavigator(
                    *if (initialBookId == null)
                        arrayOf(Route.Home)
                    else
                        arrayOf(Route.Home, Route.Detail(initialBookId))
                )

                NavDisplay(
                    backStack = navigator,
                    onBack = navigator::popUnsafe,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    entryProvider = entryProvider {
                        entry<Route.Home> {
                            HomeScreen(
                                searchEvent = searchEvent,
                                onSearchClick = {
                                    navigator.push(Route.Search)
                                },
                                onItemClick = { bookId ->
                                    navigator.push(Route.Detail(bookId))
                                }
                            )
                        }
                        entry<Route.Search> { key ->
                            SearchScreen(
                                query = searchEvent?.query ?: "",
                                onSearch = { query ->
                                    navigator.pop(key)
                                    if (query.matches(Regex("""^id:\d+$"""))) {
                                        val bookId = query.substringAfter("id:").toInt()
                                        navigator.push(Route.Detail(bookId))
                                    } else {
                                        searchEvent = SearchEvent(query)
                                    }
                                }
                            )
                        }
                        entry<Route.Detail> { key ->
                            DetailScreen(
                                bookId = key.bookId,
                                onBackClick = {
                                    navigator.pop(key)
                                },
                                onShareClick = { share(key.bookId) },
                                onThumbnailClick = { pageIndex ->
                                    navigator.push(Route.Read(key.bookId, pageIndex))
                                },
                                onTagClick = { tag ->
                                    searchEvent = SearchEvent(tag)
                                    navigator.pop(key)
                                }
                            )
                        }
                        entry<Route.Read> { key ->
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
