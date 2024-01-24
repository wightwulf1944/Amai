package i.am.shiro.amai.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import i.am.shiro.amai.BUNDLE_TAG
import i.am.shiro.amai.R
import i.am.shiro.amai.RESULT_TAG
import i.am.shiro.amai.adapter.DetailAdapter
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.DownloadJobEntity
import i.am.shiro.amai.databinding.FragmentDetailBinding
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.service.DownloadService
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.util.goToRead
import i.am.shiro.amai.util.startLocalService
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.widget.PullGestureBehavior
import io.reactivex.rxjava3.schedulers.Schedulers.io
import org.koin.android.ext.android.inject

class DetailFragment() : Fragment(R.layout.fragment_detail) {

    constructor(bookId: Int) : this() {
        this.bookId = bookId
    }

    private var bookId by argument<Int>()

    private val viewModel by amaiViewModels<DetailViewModel>()

    private val database by inject<AmaiDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setBookId(bookId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentDetailBinding.bind(view)

        b.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        b.previewRecycler.setHasFixedSize(true)
        b.previewRecycler.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            (behavior as PullGestureBehavior).setOnPullListener {
                parentFragmentManager.popBackStack()
            }
        }

        viewModel.modelLive.observe(viewLifecycleOwner) { model ->
            b.toolbar.setOnMenuItemClickListener(::onActionClick)

            b.previewRecycler.adapter = DetailAdapter(
                model = model,
                onThumbnailClick = ::invokeReadBook,
                onTagClick = ::onTagClick
            )
        }
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_download -> onDownloadClick()
            R.id.action_browser -> onOpenBrowserClick()
            R.id.action_share -> onShare()
        }
        return true
    }

    // TODO
    private fun onDownloadClick() {
        val job = DownloadJobEntity(bookId)

        database.downloadDao.insert(job)
            .subscribeOn(io())
            .subscribe {
                startLocalService<DownloadService>()
            }
    }

    private fun onOpenBrowserClick() {
        val bookUrl = Nhentai.WEBPAGE_BASE_URL + bookId
        val uri = Uri.parse(bookUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun onShare() {
        val bookUrl = Nhentai.WEBPAGE_BASE_URL + bookId
        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, bookUrl)
            .setType("text/plain")
            .let { Intent.createChooser(it, null) }
        startActivity(intent)
    }

    private fun invokeReadBook(pageIndex: Int) {
        goToRead(bookId, pageIndex)
    }

    private fun onTagClick(tag: String) {
        parentFragmentManager.setFragmentResult(RESULT_TAG, bundleOf(BUNDLE_TAG to tag))
        parentFragmentManager.popBackStack()
    }
}