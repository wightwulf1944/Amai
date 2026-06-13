package i.am.shiro.amai

import android.content.ComponentName
import android.content.Intent
import android.content.Intent.EXTRA_EXCLUDE_COMPONENTS
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.DetailScreen
import i.am.shiro.amai.compose.HomeScreen
import i.am.shiro.amai.compose.ReadScreen
import i.am.shiro.amai.compose.SearchScreen
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.FavoritesViewModel
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.ReadViewModel
import i.am.shiro.amai.viewmodel.SearchViewModel
import i.am.shiro.amai.viewmodel.factory.ViewModelFactory
import timber.log.Timber
import java.io.Serializable

private sealed interface Destination : Serializable {
    data object Home : Destination
    data object Search : Destination
    data class Detail(val bookId: Int) : Destination
    data class Read(val bookId: Int, val pageIndex: Int) : Destination
}

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val nhentaiViewModel by viewModels<NhentaiViewModel> { ViewModelFactory() }
    private val favoritesViewModel by viewModels<FavoritesViewModel> { ViewModelFactory() }
    private val searchViewModel by viewModels<SearchViewModel>()
    private val detailViewModel by viewModels<DetailViewModel> { ViewModelFactory() }
    private val readViewModel by viewModels<ReadViewModel> { ViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
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
                var currentDestination by rememberSaveable {
                    mutableStateOf(
                        if (initialBookId == null) {
                            Destination.Home
                        } else {
                            detailViewModel.load(initialBookId)
                            Destination.Detail(initialBookId)
                        }
                    )
                }

                Crossfade(targetState = currentDestination, label = "navigation") { destination ->
                    when (destination) {
                        Destination.Home -> {
                            HomeScreen(
                                mainViewModel = mainViewModel,
                                nhentaiViewModel = nhentaiViewModel,
                                favoritesViewModel = favoritesViewModel,
                                onSearchClick = { currentDestination = Destination.Search },
                                onItemClick = { bookId ->
                                    detailViewModel.load(bookId)
                                    currentDestination = Destination.Detail(bookId)
                                }
                            )
                        }

                        Destination.Search -> {
                            BackHandler {
                                currentDestination = Destination.Home
                            }
                            val suggestions by searchViewModel.suggestions.collectAsState()
                            SearchScreen(
                                suggestions = suggestions,
                                onQueryChange = { searchViewModel.onQueryChange(it) },
                                onSearch = {
                                    mainViewModel.search(it)
                                    currentDestination = Destination.Home
                                }
                            )
                        }

                        is Destination.Detail -> {
                            BackHandler {
                                currentDestination = Destination.Home
                            }

                            key(destination.bookId) {
                                val model by detailViewModel.uiState.collectAsState()
                                model?.let { detailModel ->
                                    DetailScreen(
                                        model = detailModel,
                                        onBackClick = {
                                            currentDestination = Destination.Home
                                        },
                                        onShareClick = {
                                            val bookUrl = "${Nhentai.WEBPAGE_BASE_URL}${destination.bookId}/"
                                            val exclude = arrayOf(ComponentName(this, MainActivity::class.java))
                                            val intent = Intent(Intent.ACTION_SEND)
                                                .putExtra(EXTRA_TEXT, bookUrl)
                                                .putExtra(EXTRA_EXCLUDE_COMPONENTS, exclude)
                                                .setType("text/plain")
                                                .let { createChooser(it, null) }
                                            startActivity(intent)
                                        },
                                        onFavoriteToggle = detailViewModel::onFavoriteToggle,
                                        onThumbnailClick = { pageIndex ->
                                            readViewModel.setBookId(destination.bookId)
                                            currentDestination = Destination.Read(destination.bookId, pageIndex)
                                        },
                                        onTagClick = {
                                            mainViewModel.search(it)
                                            currentDestination = Destination.Home
                                        }
                                    )
                                }
                            }
                        }

                        is Destination.Read -> {
                            BackHandler {
                                currentDestination = Destination.Detail(destination.bookId)
                            }

                            key(destination.bookId) {
                                DisposableEffect(Unit) {
                                    val window = window
                                    val controller = WindowInsetsControllerCompat(window, window.decorView)
                                    controller.hide(WindowInsetsCompat.Type.statusBars())
                                    controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                                    onDispose {
                                        controller.show(WindowInsetsCompat.Type.statusBars())
                                    }
                                }

                                ReadScreen(
                                    viewModel = readViewModel,
                                    initialPage = destination.pageIndex
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
