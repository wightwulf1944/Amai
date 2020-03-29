package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.CachedPreviewAdapter
import i.am.shiro.amai.dagger.component
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.fragment.dialog.NhentaiSortDialog
import i.am.shiro.amai.fragment.dialog.SearchConstantsDialog
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.factory.SavedStateViewModelFactory
import kotlinx.android.synthetic.main.fragment_nhentai.*

class NhentaiFragment : Fragment(R.layout.fragment_nhentai) {

    private val viewModel by viewModels<NhentaiViewModel> {
        SavedStateViewModelFactory(this, component)
    }

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
            onItemClick = ::invokeViewDetails,
            onPositionBind = viewModel::onPositionBind
        )

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.booksLive.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.isLoadingLive.observe(viewLifecycleOwner) { isLoading ->
            progress.visibility = if (isLoading) VISIBLE else GONE
        }
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_sort -> NhentaiSortDialog().show(childFragmentManager)
            R.id.action_constants -> SearchConstantsDialog().show(childFragmentManager)
        }
        return true
    }


    private fun invokeViewDetails(book: CachedPreviewView) {
        goToDetail(book.bookId)
    }
}
