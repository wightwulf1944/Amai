package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import i.am.shiro.amai.BUNDLE_TAG
import i.am.shiro.amai.R
import i.am.shiro.amai.RESULT_TAG
import i.am.shiro.amai.adapter.CachedPreviewAdapter
import i.am.shiro.amai.databinding.FragmentNhentaiBinding
import i.am.shiro.amai.fragment.dialog.NhentaiSortDialog
import i.am.shiro.amai.fragment.dialog.SearchConstantsDialog
import i.am.shiro.amai.util.amaiStatefulViewModels
import i.am.shiro.amai.util.dpToPx
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.NhentaiViewModel

class NhentaiFragment : Fragment(R.layout.fragment_nhentai) {

    private val viewModel by amaiStatefulViewModels<NhentaiViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentNhentaiBinding.bind(view)

        b.toolbar.setOnMenuItemClickListener(::onActionClick)

        b.searchInput.onSubmitListener = { query: String ->
            b.recyclerView.scrollToPosition(0)
            viewModel.onSearch(query)
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

        parentFragmentManager.setFragmentResultListener(RESULT_TAG, viewLifecycleOwner) { _, result ->
            val tag = result.getString(BUNDLE_TAG)!!
            b.searchInput.setText(tag)
            b.recyclerView.scrollToPosition(0)
            viewModel.onSearch(tag)
        }
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_sort -> NhentaiSortDialog().show(childFragmentManager)
            R.id.action_constants -> SearchConstantsDialog().show(childFragmentManager)
        }
        return true
    }
}
