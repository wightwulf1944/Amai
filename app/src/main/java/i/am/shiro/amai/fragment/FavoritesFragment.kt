package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import i.am.shiro.amai.R
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.adapter.FavoritesPreviewAdapter
import i.am.shiro.amai.data.view.FavoritesPreviewView
import i.am.shiro.amai.databinding.FragmentFavoritesBinding
import i.am.shiro.amai.fragment.dialog.DeleteBookDialog
import i.am.shiro.amai.fragment.dialog.PlaceholderDialog
import i.am.shiro.amai.fragment.dialog.FavoritesSortDialog
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.loadBoolean
import i.am.shiro.amai.util.saveBoolean
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel by amaiViewModels<FavoritesViewModel>()

    private var shouldScrollToTop = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.loadBoolean(::shouldScrollToTop)

        val b = FragmentFavoritesBinding.bind(view)

        b.toolbar.setOnMenuItemClickListener(::onActionClick)

        b.searchInput.onSubmitListener = { searchQuery ->
            shouldScrollToTop = true
            viewModel.onSearch(searchQuery)
        }

        val adapter = FavoritesPreviewAdapter(
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

    fun onSort(sort: FavoritesSort) {
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
        childFragmentManager.show(FavoritesSortDialog())
    }

    private fun invokeHelp() {
        childFragmentManager.show(PlaceholderDialog())
    }

    private fun invokeViewDetails(preview: FavoritesPreviewView) {
        goToDetail(preview.bookId)
    }

    private fun invokeDeleteBook(preview: FavoritesPreviewView) {
        childFragmentManager.show(DeleteBookDialog(preview.bookId, preview.title))
    }
}
