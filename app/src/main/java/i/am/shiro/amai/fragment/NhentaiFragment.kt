package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import i.am.shiro.amai.BUNDLE_TAG
import i.am.shiro.amai.R
import i.am.shiro.amai.RESULT_TAG
import i.am.shiro.amai.adapter.CachedPreviewAdapter
import i.am.shiro.amai.fragment.dialog.NhentaiSortDialog
import i.am.shiro.amai.fragment.dialog.SearchConstantsDialog
import i.am.shiro.amai.util.amaiStatefulViewModels
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import kotlinx.android.synthetic.main.fragment_nhentai.*

class NhentaiFragment : Fragment(R.layout.fragment_nhentai) {

    private val viewModel by amaiStatefulViewModels<NhentaiViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setOnMenuItemClickListener(::onActionClick)

        searchInput.onSubmitListener = { query: String ->
            recyclerView.scrollToPosition(0)
            viewModel.onSearch(query)
        }

        val offset = (64 * resources.displayMetrics.density).toInt()
        swipeRefreshLayout.setProgressViewOffset(false, 0, offset)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.onRefresh()
        }

        val adapter = CachedPreviewAdapter(
            parentFragment = this,
            onItemClick = { goToDetail(it.bookId) },
            onPositionBind = viewModel::onPositionBind
        )

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.booksLive.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.isLoadingLive.observe(viewLifecycleOwner) { isLoading ->
            progress.visibility = if (isLoading) VISIBLE else GONE
        }

        parentFragmentManager.setFragmentResultListener(RESULT_TAG, viewLifecycleOwner) { _, result ->
            val tag = result.getString(BUNDLE_TAG)!!
            searchInput.setText(tag)
            recyclerView.scrollToPosition(0)
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
