package i.am.shiro.amai.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.fragment.app.Fragment
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.BookPageAdapter
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.viewmodel.ReadViewModel
import kotlinx.android.synthetic.main.fragment_read.*

class ReadFragment() : Fragment(R.layout.fragment_read) {

    private val viewModel by amaiViewModels<ReadViewModel>()

    private var bookId by argument<Int>()

    private var pageIndex by argument<Int>()

    constructor(bookId: Int, pageIndex: Int) : this() {
        this.bookId = bookId
        this.pageIndex = pageIndex
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity()
            .window
            .addFlags(FLAG_FULLSCREEN)
    }

    override fun onDetach() {
        super.onDetach()

        requireActivity()
            .window
            .clearFlags(FLAG_FULLSCREEN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setBookId(bookId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pageRecycler.setHasFixedSize(true)
        pageRecycler.requestFocus()
        pageRecycler.setOnPageScrollListener { value -> pageText.text = value.toString() }

        viewModel.pagesLive.observe(this) {
            pageRecycler.adapter = BookPageAdapter(this, it)

            if (savedInstanceState == null) {
                val position = pageIndex
                pageRecycler.scrollToPosition(position)
                pageText.text = (position + 1).toString()
            }
        }
    }
}