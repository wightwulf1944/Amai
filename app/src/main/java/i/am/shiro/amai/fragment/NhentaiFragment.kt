package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import i.am.shiro.amai.R
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.BrowseScreen
import i.am.shiro.amai.fragment.dialog.NhentaiSortDialog
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.goToSearch
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel

// TODO try Jetpack Paging 3 library for infinite scrolling
class NhentaiFragment : Fragment() {

    private val viewModel by amaiViewModels<NhentaiViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext())
        composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
        composeView.setContent {
            val books by viewModel.booksLive.observeAsState(emptyList())
            val isLoading by viewModel.isLoadingLive.observeAsState(false)
            val scrollState = rememberLazyStaggeredGridState()

            var title by remember { mutableStateOf(getString(R.string.nhentai)) }
            var scrollTrigger by remember { mutableIntStateOf(0) }

            LaunchedEffect(scrollTrigger) {
                if (scrollTrigger > 0) {
                    scrollState.scrollToItem(0)
                }
            }

            AmaiTheme {
                BrowseScreen(
                    title = title,
                    books = books,
                    isLoading = isLoading,
                    onRefresh = viewModel::onRefresh,
                    onSortClick = { childFragmentManager.show(NhentaiSortDialog()) },
                    onSearchClick = { goToSearch() },
                    onItemClick = { bookId -> goToDetail(bookId) },
                    onPositionBind = viewModel::onPositionBind,
                    gridState = scrollState
                )
            }

            LaunchedEffect(Unit) {
                activityViewModel.searchEventLive.observe(viewLifecycleOwner) { event ->
                    if (!event.isNhentaiConsumed) {
                        title = event.query
                        scrollTrigger++
                        viewModel.onSearch(event.query)
                        event.isNhentaiConsumed = true
                    }
                }
            }
        }

        return composeView
    }
}
