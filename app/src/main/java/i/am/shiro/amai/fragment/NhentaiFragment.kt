package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.CachedPreviewAdapter
import i.am.shiro.amai.databinding.FragmentNhentaiBinding
import i.am.shiro.amai.fragment.dialog.NhentaiSortDialog
import i.am.shiro.amai.util.amaiStatefulViewModels
import i.am.shiro.amai.util.dpToPx
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.Home
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NavigationPath
import i.am.shiro.amai.viewmodel.Nhentai
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.Search

// TODO try Jetpack Paging 3 library for infinite scrolling
class NhentaiFragment : Fragment(R.layout.fragment_nhentai) {

    private val viewModel by amaiStatefulViewModels<NhentaiViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentNhentaiBinding.bind(view)

        b.titleView.setOnClickListener {
            activityViewModel.navigationPathLive.value =
                NavigationPath("${b.titleView.text}", Home, Search)
        }

        b.sortButton.setOnClickListener {
            NhentaiSortDialog().show(childFragmentManager)
        }

        b.searchButton.setOnClickListener {
            activityViewModel.navigationPathLive.value = NavigationPath("", Home, Search)
        }

        b.swipeRefreshLayout.setProgressViewOffset(false, 0, 64.dpToPx())
        b.swipeRefreshLayout.setOnRefreshListener {
            b.swipeRefreshLayout.isRefreshing = false
            viewModel.onRefresh()
        }

        val adapter = CachedPreviewAdapter(
            onItemClick = { goToDetail(it.bookId) },
            onPositionBind = viewModel::onPositionBind
        )

        b.recyclerView.setHasFixedSize(true)
        b.recyclerView.adapter = adapter

        viewModel.booksLive.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.isLoadingLive.observe(viewLifecycleOwner) { isLoading ->
            b.progressBar.isVisible = isLoading
        }

        activityViewModel.navigationPathLive.observe(viewLifecycleOwner) { path ->
            path.traverse(Nhentai) {
                val search = path.payload
                b.titleView.text = search
                b.recyclerView.scrollToPosition(0)
                viewModel.onSearch(search)
            }
        }
    }
}
