package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.BookAdapter
import i.am.shiro.amai.fragment.dialog.DeleteBookDialogFragment
import i.am.shiro.amai.fragment.dialog.PlaceholderDialogFragment
import i.am.shiro.amai.fragment.dialog.SavedSortOrderDialogFragment
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.SavedFragmentModel
import kotlinx.android.synthetic.main.fragment_saved.*
import timber.log.Timber

class SavedFragment : Fragment(R.layout.fragment_saved) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setOnMenuItemClickListener { onActionClick(it) }

        searchInput.setOnSubmitListener { s -> invokeSearch(s) }

        val bookAdapter = BookAdapter(this, layoutInflater).apply {
            setOnItemClickListener { book -> invokeViewDetails(book) }
            setOnItemLongClickListener { book -> invokeDeleteBook(book) }
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = bookAdapter

        ViewModelProviders.of(this)
                .get(SavedFragmentModel::class.java)
                .observeBooks(this, Observer<List<Book>> { bookAdapter.submitList(it) })
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_sort -> invokeSort()
            R.id.action_help -> invokeHelp()
        }
        return true
    }

    private fun invokeSearch(s: String) {
        PlaceholderDialogFragment().show(childFragmentManager)
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
