package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.FavoritesScreen
import i.am.shiro.amai.fragment.dialog.FavoritesSortDialog
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.loadBoolean
import i.am.shiro.amai.util.saveBoolean
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private val viewModel by amaiViewModels<FavoritesViewModel>()

    private var shouldScrollToTop = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedInstanceState?.loadBoolean(::shouldScrollToTop)

        val composeView = ComposeView(requireContext())
        composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
        composeView.setContent {
            AmaiTheme {
                val books by viewModel.booksLive.observeAsState(emptyList())
                val gridState = rememberLazyStaggeredGridState()

                LaunchedEffect(books) {
                    if (shouldScrollToTop) {
                        gridState.scrollToItem(0)
                        shouldScrollToTop = false
                    }
                }

                FavoritesScreen(
                    books = books,
                    onSortClick = {
                        childFragmentManager.show(FavoritesSortDialog())
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

    fun onSort(sort: FavoritesSort) {
        shouldScrollToTop = true
        viewModel.onSort(sort)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.saveBoolean(::shouldScrollToTop)
    }
}
