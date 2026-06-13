package i.am.shiro.amai.fragment

import android.content.ComponentName
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_EXCLUDE_COMPONENTS
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import i.am.shiro.amai.MainActivity
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.DetailScreen
import i.am.shiro.amai.compose.HomeScreen
import i.am.shiro.amai.compose.ReadScreen
import i.am.shiro.amai.compose.SearchScreen
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.FavoritesViewModel
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.ReadViewModel
import i.am.shiro.amai.viewmodel.SearchViewModel
import java.io.Serializable

private sealed class Destination : Serializable {
    data object Home : Destination()
    data object Search : Destination()
    data class Detail(val bookId: Int) : Destination()
    data class Read(val bookId: Int, val pageIndex: Int) : Destination()
}

class HomeComposeFragment() : Fragment() {

    constructor(bookId: Int) : this() {
        this.bookId = bookId
    }

    var bookId by argument<Int>()

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val nhentaiViewModel by amaiViewModels<NhentaiViewModel>()
    private val favoritesViewModel by amaiViewModels<FavoritesViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()
    private val detailViewModel by amaiViewModels<DetailViewModel>()
    private val readViewModel by amaiViewModels<ReadViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AmaiTheme {
                    val initialDestination = remember {
                        // TODO replace this with a synthetic backstack
                        if (arguments?.containsKey("bookId") == true) {
                            Destination.Detail(bookId)
                        } else {
                            Destination.Home
                        }
                    }
                    var currentDestination by rememberSaveable { mutableStateOf(initialDestination) }

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

                                val model by detailViewModel.uiState.collectAsState()
                                model?.let { detailModel ->
                                    DetailScreen(
                                        model = detailModel,
                                        onBackClick = {
                                            currentDestination = Destination.Home
                                        },
                                        onShareClick = {
                                            val bookUrl = "${Nhentai.WEBPAGE_BASE_URL}${destination.bookId}/"
                                            val exclude = arrayOf(ComponentName(requireContext(), MainActivity::class.java))
                                            val intent = Intent(ACTION_SEND)
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

                            is Destination.Read -> {
                                BackHandler {
                                    currentDestination = Destination.Detail(destination.bookId)
                                }

                                DisposableEffect(Unit) {
                                    val window = requireActivity().window
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
