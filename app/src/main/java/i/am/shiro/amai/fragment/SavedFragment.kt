package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import i.am.shiro.amai.R
import i.am.shiro.amai.SavedSort
import i.am.shiro.amai.adapter.SavedPreviewAdapter
import i.am.shiro.amai.data.view.SavedPreviewView
import i.am.shiro.amai.fragment.dialog.DeleteBookDialog
import i.am.shiro.amai.fragment.dialog.PlaceholderDialog
import i.am.shiro.amai.fragment.dialog.SavedSortDialog
import i.am.shiro.amai.util.loadBoolean
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.saveBoolean
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.SavedViewModel
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private val viewModel by viewModels<SavedViewModel>()

    private var shouldScrollToTop = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.loadBoolean(::shouldScrollToTop)

        toolbar.setOnMenuItemClickListener(::onActionClick)

        searchInput.onSubmitListener = { searchQuery ->
            shouldScrollToTop = true
            viewModel.onSearch(searchQuery)
        }

        val adapter = SavedPreviewAdapter(
            parentFragment = this,
            onItemClick = ::invokeViewDetails,
            onItemLongClick = ::invokeDeleteBook
        )

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.booksLive.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books) {
                if (shouldScrollToTop) {
                    recyclerView.scrollToPosition(0)
                    shouldScrollToTop = false
                }
            }
        }
    }

    fun onSort(sort: SavedSort) {
        shouldScrollToTop = true
        viewModel.onSort(sort)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.saveBoolean(::shouldScrollToTop)
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_sort -> invokeSort()
            R.id.action_help -> invokeHelp()
        }
        return true
    }

    private fun invokeSort() {
        SavedSortDialog().show(childFragmentManager)
    }

    private fun invokeHelp() {
        PlaceholderDialog().show(childFragmentManager)
    }

    private fun invokeViewDetails(preview: SavedPreviewView) {
        goToDetail(preview.bookId)
    }

    private fun invokeDeleteBook(preview: SavedPreviewView) {
        DeleteBookDialog(preview.bookId, preview.title).show(childFragmentManager)
    }
}
