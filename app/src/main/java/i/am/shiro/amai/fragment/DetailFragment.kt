package i.am.shiro.amai.fragment

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import i.am.shiro.amai.MainActivity
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.DetailAdapter
import i.am.shiro.amai.databinding.FragmentDetailBinding
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.util.goToRead
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.MainViewModel

class DetailFragment() : Fragment(R.layout.fragment_detail) {

    constructor(bookId: Int) : this() {
        this.bookId = bookId
    }

    private var bookId by argument<Int>()

    private val viewModel by amaiViewModels<DetailViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentDetailBinding.bind(view)

        b.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        b.toolbar.setOnMenuItemClickListener(::onActionClick)

        b.contentRecycler.setHasFixedSize(true)

        viewModel.modelLive.observe(viewLifecycleOwner) { model ->
            val menuItem = b.toolbar.menu.findItem(R.id.action_favorite)
            menuItem.icon?.state = if (model.isFavorite) {
                intArrayOf(android.R.attr.state_checked)
            } else {
                intArrayOf()
            }

            b.contentRecycler.adapter = DetailAdapter(
                model = model,
                onThumbnailClick = ::invokeReadBook,
                onTagClick = ::onTagClick
            )
        }
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_favorite -> viewModel.toggleFavorite()
            R.id.action_share -> onShare()
        }
        return true
    }

    private fun onShare() {
        val bookUrl = "${Nhentai.WEBPAGE_BASE_URL}$bookId/"
        val exclude = arrayOf(ComponentName(requireContext(), MainActivity::class.java))
        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, bookUrl)
            .putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, exclude)
            .setType("text/plain")
            .let { Intent.createChooser(it, null) }
        startActivity(intent)
    }

    private fun invokeReadBook(pageIndex: Int) {
        goToRead(bookId, pageIndex)
    }

    private fun onTagClick(tag: String) {
        activityViewModel.search(tag)
        parentFragmentManager.popBackStack()
    }
}