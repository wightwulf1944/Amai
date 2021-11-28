package i.am.shiro.amai.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.DetailAdapter
import i.am.shiro.amai.dagger.component
import i.am.shiro.amai.data.entity.DownloadJobEntity
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.service.DownloadService
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.util.goToRead
import i.am.shiro.amai.util.startLocalService
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.widget.PullGestureBehavior
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment() : Fragment(R.layout.fragment_detail) {

    constructor(bookId: Int) : this() {
        this.bookId = bookId
    }

    private var bookId by argument<Int>()

    private val viewModel by amaiViewModels<DetailViewModel>()

    private val database get() = component.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setBookId(bookId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        previewRecycler.setHasFixedSize(true)
        previewRecycler.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            (behavior as PullGestureBehavior).setOnPullListener {
                parentFragmentManager.popBackStack()
            }
        }

        viewModel.modelLive.observe(viewLifecycleOwner, ::onModelLoaded)
    }

    private fun onModelLoaded(model: DetailModel) {
        toolbar.setOnMenuItemClickListener(::onActionClick)

        previewRecycler.adapter = DetailAdapter(
            parentFragment = this,
            model = model,
            onThumbnailClick = ::invokeReadBook
        )
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_download -> onDownloadClick()
            R.id.action_browser -> onOpenBrowserClick()
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
        val bookUrl = Nhentai.WEBPAGE_BASE_URL + viewModel.modelLive.value!!.book.bookId
        val uri = Uri.parse(bookUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun invokeReadBook(pageIndex: Int) {
        goToRead(bookId, pageIndex)
    }
}