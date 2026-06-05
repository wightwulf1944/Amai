package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.FavoritesScreen
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private val viewModel by amaiViewModels<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext())
        composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
        composeView.setContent {
            AmaiTheme {
                val books by viewModel.booksLive.observeAsState(emptyList())
                val gridState = rememberLazyStaggeredGridState()
                var shouldScrollToTop by rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(books) {
                    if (shouldScrollToTop) {
                        gridState.scrollToItem(0)
                        shouldScrollToTop = false
                    }
                }

                FavoritesScreen(
                    books = books,
                    onSortChanged = { sort ->
                        shouldScrollToTop = true
                        viewModel.onSort(sort)
                    },
                    onSearchSubmit = { searchQuery ->
                        shouldScrollToTop = true
                        viewModel.onSearch(searchQuery)
                    },
                    onItemClick = { bookId -> goToDetail(bookId) },
                    gridState = gridState
                )
            }
        }

        return composeView
    }
}
