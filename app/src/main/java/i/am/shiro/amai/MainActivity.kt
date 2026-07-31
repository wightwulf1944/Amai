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
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.repository.GalleryRepository
import i.am.shiro.amai.ui.DetailScreen
import i.am.shiro.amai.ui.HomeScreen
import i.am.shiro.amai.ui.ReadScreen
import i.am.shiro.amai.ui.SearchScreen
import i.am.shiro.amai.ui.navigation.Route
import i.am.shiro.amai.ui.navigation.rememberNavigator
import i.am.shiro.amai.ui.rememberHomeScreenState
import i.am.shiro.amai.ui.theme.AmaiTheme
import i.am.shiro.amai.ui.utils.sharedElementToken
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val repository: GalleryRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            lifecycleScope.launch {
                repository.clearAllCache()
            }
        }

        val initialBookId = parseBookId(intent)

        setContent {
            AmaiTheme {
                AdaptiveStatusBarsEffect()

                val homeScreenState = rememberHomeScreenState()

                val navigator = rememberNavigator(
                    *if (initialBookId == null)
                        arrayOf(Route.Home)
                    else
                        arrayOf(Route.Home, Route.Detail(initialBookId))
                )

                OnNewIntentEffect { intent ->
                    val newBookId = parseBookId(intent)
                    if (newBookId != null) navigator.push(Route.Detail(newBookId))
                }

                SharedTransitionLayout {
                    val searchPillSharedElementToken = sharedElementToken("searchpill")

                    NavDisplay(
                        backStack = navigator,
                        onBack = navigator::popUnsafe,
                        sharedTransitionScope = this,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        entryProvider = entryProvider {
                            entry<Route.Home> {
                                HomeScreen(
                                    state = homeScreenState,
                                    onSearchClick = { initialQuery ->
                                        navigator.push(Route.Search(initialQuery))
                                    },
                                    onItemClick = { bookId ->
                                        navigator.push(Route.Detail(bookId))
                                    },
                                    searchPillToken = searchPillSharedElementToken,
                                )
                            }
                            entry<Route.Search> { key ->
                                SearchScreen(
                                    initialQuery = key.initialQuery,
                                    onSearch = { query, sort ->
                                        if (query.isEmpty()) {
                                            homeScreenState.goToLatest()
                                        } else if (query.matches(Regex("""^id:\d+$"""))) {
                                            val bookId = query.substringAfter("id:").toInt()
                                            navigator.push(Route.Detail(bookId))
                                        } else {
                                            homeScreenState.goToSearch(query, sort)
                                        }
                                    },
                                    onDismissRequest = { navigator.pop(key) },
                                    searchPillToken = searchPillSharedElementToken,
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
                                        navigator.pop(key)
                                        homeScreenState.goToTag(tag)
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
    }

    @Composable
    private fun OnNewIntentEffect(onNewIntent: (Intent) -> Unit) {
        val currentOnNewIntent by rememberUpdatedState(onNewIntent)
        DisposableEffect(Unit) {
            val listener = Consumer<Intent> { currentOnNewIntent(it) }
            addOnNewIntentListener(listener)
            onDispose {
                removeOnNewIntentListener(listener)
            }
        }
    }

    @Composable
    private fun AdaptiveStatusBarsEffect() {
        val shouldShowStatusBars = currentWindowAdaptiveInfo().windowSizeClass
            .isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)

        DisposableEffect(shouldShowStatusBars) {
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            if (shouldShowStatusBars) {
                controller.show(Type.statusBars())
                controller.systemBarsBehavior = BEHAVIOR_DEFAULT

            } else {
                controller.hide(Type.statusBars())
                controller.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            onDispose {
                controller.show(Type.statusBars())
                controller.systemBarsBehavior = BEHAVIOR_DEFAULT
            }
        }
    }

    private fun parseBookId(intent: Intent): Int? {
        val uri = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> intent.getStringExtra(EXTRA_TEXT)?.toUri()
            else -> null
        }
        if (uri == null) return null
        if (uri.host?.endsWith("nhentai.net") == false) return null
        if (uri.pathSegments.getOrNull(0) != "g") return null
        return uri.pathSegments.getOrNull(1)?.toIntOrNull()
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
