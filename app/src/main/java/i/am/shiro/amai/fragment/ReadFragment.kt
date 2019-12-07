package i.am.shiro.amai.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.fragment.app.Fragment
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.BookPageAdapter
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.model.Image
import i.am.shiro.amai.util.argument
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_read.*

class ReadFragment() : Fragment(R.layout.fragment_read) {

    private lateinit var realm: Realm

    private var bookId by argument<Int>()

    private var pageIndex by argument<Int>()

    constructor(bookId: Int, pageIndex: Int) : this() {
        this.bookId = bookId
        this.pageIndex = pageIndex
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        realm = Realm.getDefaultInstance()

        requireActivity()
            .window
            .addFlags(FLAG_FULLSCREEN)
    }

    override fun onDetach() {
        super.onDetach()

        realm.close()

        requireActivity()
            .window
            .clearFlags(FLAG_FULLSCREEN)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pageRecycler.setHasFixedSize(true)
        pageRecycler.adapter = BookPageAdapter(this, extractImages())
        pageRecycler.requestFocus()
        pageRecycler.setOnPageScrollListener { value -> pageText.text = value.toString() }

        if (savedInstanceState == null) {
            val position = pageIndex
            pageRecycler.scrollToPosition(position)
            pageText.text = (position + 1).toString()
        }
    }

    private fun extractImages(): List<Image> {
        return realm.where(Book::class.java)
            .equalTo("id", bookId)
            .findFirst()!!
            .pageImages
    }
}