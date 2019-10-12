package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.BookAdapter
import i.am.shiro.amai.fragment.dialog.NhentaiSortOrderDialogFragment
import i.am.shiro.amai.fragment.dialog.SearchConstantsDialogFragment
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.util.show
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import kotlinx.android.synthetic.main.fragment_nhentai.*

class NhentaiFragment : Fragment(R.layout.fragment_nhentai) {

    private val viewModel by viewModels<NhentaiViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.onNewInstanceCreated()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setOnMenuItemClickListener(::onActionClick)

        searchInput.setOnSubmitListener(viewModel::search)

        val adapter = BookAdapter(
                parentFragment = this,
                onItemClick = ::invokeViewDetails,
                onPositionBind = viewModel::onPositionBind
        )

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.observeBooks(this, Observer(adapter::submitList))
        viewModel.observeLoadingState(this, Observer { isLoading ->
            progress.visibility = if (isLoading) VISIBLE else GONE
        })
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_sort -> {
                NhentaiSortOrderDialogFragment().show(childFragmentManager)
            }
            R.id.action_constants -> {
                SearchConstantsDialogFragment().show(childFragmentManager)
            }
        }
        return true
    }


    private fun invokeViewDetails(book: Book) {
        val fragment = DetailFragment.newInstance(book)

        requireActivity().supportFragmentManager.commit {
            replace(android.R.id.content, fragment)
            addToBackStack(null)
        }
    }
}
