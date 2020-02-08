package i.am.shiro.amai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import i.am.shiro.amai.R
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.viewmodel.LoadingViewModel

class LoadingFragment() : Fragment(R.layout.fragment_loading) {

    private var bookId by argument<Int>()

    constructor(bookId: Int) : this() {
        this.bookId = bookId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<LoadingViewModel>()

        viewModel.load(bookId).observe(this) {
            goToDetail(bookId)
        }
    }
}