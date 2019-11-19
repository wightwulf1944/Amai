package i.am.shiro.amai.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.DetailAdapter
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.service.addToQueue
import i.am.shiro.amai.util.buildArguments
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_detail.*

private const val BOOK_ID = "bookId"

class DetailFragment() : Fragment(R.layout.fragment_detail) {

    private lateinit var realm: Realm

    private lateinit var book: Book

    constructor(bookId: Int) : this() {
        buildArguments {
            putInt(BOOK_ID, bookId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()

        val bookId = arguments!!.getInt(BOOK_ID, -1)
        book = realm.where(Book::class.java)
            .equalTo("id", bookId)
            .findFirst()!!
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener { onBackClick() }
        toolbar.setOnMenuItemClickListener(::onActionClick)

        previewRecycler.setHasFixedSize(true)
        previewRecycler.adapter = DetailAdapter(
            parentFragment = this,
            book = book,
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

    private fun onDownloadClick() {
        requireContext().addToQueue(book)
    }

    private fun onOpenBrowserClick() {
        val webUrl = book.webUrl
        val uri = Uri.parse(webUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun invokeReadBook(pageIndex: Int) {
        val fragment = ReadFragment(book.id, pageIndex)

        requireFragmentManager().commit {
            replace(android.R.id.content, fragment)
            addToBackStack(null)
        }
    }
}