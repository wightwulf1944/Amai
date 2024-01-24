package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import i.am.shiro.amai.R
import i.am.shiro.amai.SavedSort
import i.am.shiro.amai.adapter.SavedPreviewAdapter
import i.am.shiro.amai.data.view.SavedPreviewView
import i.am.shiro.amai.databinding.FragmentSavedBinding
import i.am.shiro.amai.fragment.dialog.DeleteBookDialog
import i.am.shiro.amai.fragment.dialog.PlaceholderDialog
import i.am.shiro.amai.fragment.dialog.SavedSortDialog
import i.am.shiro.amai.util.*
import i.am.shiro.amai.viewmodel.SavedViewModel

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private val viewModel by amaiStatefulViewModels<SavedViewModel>()

    private var shouldScrollToTop = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.loadBoolean(::shouldScrollToTop)

        val b = FragmentSavedBinding.bind(view)

        b.toolbar.setOnMenuItemClickListener(::onActionClick)

        b.searchInput.onSubmitListener = { searchQuery ->
            shouldScrollToTop = true
            viewModel.onSearch(searchQuery)
        }

        val adapter = SavedPreviewAdapter(
            onItemClick = ::invokeViewDetails,
            onItemLongClick = ::invokeDeleteBook
        )

        b.recyclerView.setHasFixedSize(true)
        b.recyclerView.adapter = adapter

        viewModel.booksLive.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books) {
                if (shouldScrollToTop) {
                    b.recyclerView.scrollToPosition(0)
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
