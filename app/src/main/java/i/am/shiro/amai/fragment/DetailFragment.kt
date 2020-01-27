package i.am.shiro.amai.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import i.am.shiro.amai.DATABASE
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.DetailAdapter
import i.am.shiro.amai.data.entity.DownloadJobEntity
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.service.DownloadService
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.util.startLocalService
import i.am.shiro.amai.viewmodel.DetailViewModel
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment() : Fragment(R.layout.fragment_detail) {

    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var model: DetailModel

    private var bookId by argument<Int>()

    constructor(bookId: Int) : this() {
        this.bookId = bookId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setBookId(bookId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener { onBackClick() }
        previewRecycler.setHasFixedSize(true)

        viewModel.modelLive.observe(viewLifecycleOwner, ::onModelLoaded)
    }

    private fun onModelLoaded(model: DetailModel) {
        this.model = model

        toolbar.setOnMenuItemClickListener(::onActionClick)

        previewRecycler.adapter = DetailAdapter(
            parentFragment = this,
            model = model,
            onThumbnailClickListener = ::invokeReadBook
        )

        val layoutManager = previewRecycler.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int) = if (position == 0) 2 else 1
        }
    }

    private fun onActionClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_download -> onDownloadClick()
            R.id.action_browser -> onOpenBrowserClick()
        }
        return true
    }

    private fun onBackClick() {
        requireActivity().onBackPressed()
    }

    // TODO
    private fun onDownloadClick() {
        val job = DownloadJobEntity(model.book.bookId)

        DATABASE.downloadDao.insert(job)
            .subscribeOn(io())
            .subscribe {
                startLocalService<DownloadService>()
            }
    }

    private fun onOpenBrowserClick() {
        val webUrl = model.book.webUrl
        val uri = Uri.parse(webUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun invokeReadBook(pageIndex: Int) {
        val fragment = ReadFragment(model.book.bookId, pageIndex)

        parentFragmentManager.commit {
            replace(android.R.id.content, fragment)
            addToBackStack(null)
        }
    }
}