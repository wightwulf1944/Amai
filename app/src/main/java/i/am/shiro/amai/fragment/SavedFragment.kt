package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe

import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.BookAdapter
import i.am.shiro.amai.fragment.dialog.DeleteBookDialogFragment
import i.am.shiro.amai.fragment.dialog.PlaceholderDialogFragment
import i.am.shiro.amai.fragment.dialog.SavedSortOrderDialogFragment
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.SavedViewModel
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private val viewModel by viewModels<SavedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setOnMenuItemClickListener(::onActionClick)

        searchInput.onSubmitListener = viewModel::filterBooks

        val adapter = BookAdapter(this,
            onItemClick = ::invokeViewDetails,
            onItemLongClick = ::invokeDeleteBook
        )

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.booksLive.observe(this, adapter::submitList)
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_sort -> invokeSort()
            R.id.action_help -> invokeHelp()
        }
        return true
    }

    private fun invokeSort() {
        SavedSortOrderDialogFragment().show(childFragmentManager)
    }

    private fun invokeHelp() {
        PlaceholderDialogFragment().show(childFragmentManager)
    }

    private fun invokeViewDetails(book: Book) {
        val fragment = DetailFragment.newInstance(book)

        requireActivity().supportFragmentManager.commit {
            replace(android.R.id.content, fragment)
            addToBackStack(null)
        }
    }

    private fun invokeDeleteBook(book: Book) {
        val dialogFragment = DeleteBookDialogFragment()
        dialogFragment.setArguments(book)
        dialogFragment.show(childFragmentManager)
    }
}
