package i.am.shiro.amai.fragment

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import i.am.shiro.amai.MainActivity
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.DetailScreen
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.util.goToRead
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.MainViewModel

class DetailFragment() : Fragment() {

    constructor(bookId: Int) : this() {
        this.bookId = bookId
    }

    private var bookId by argument<Int>()

    private val viewModel by amaiViewModels<DetailViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext())
        composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
        composeView.setContent {
            AmaiTheme {
                val model by viewModel.modelLive.observeAsState()
                model?.let {
                    DetailScreen(
                        model = it,
                        onBackClick = { parentFragmentManager.popBackStack() },
                        onShareClick = ::onShareClick,
                        onFavoriteToggle = viewModel::onFavoriteToggle,
                        onThumbnailClick = ::onThumbnailClick,
                        onTagClick = ::onTagClick
                    )
                }
            }
        }

        return composeView
    }

    private fun onShareClick() {
        val bookUrl = "${Nhentai.WEBPAGE_BASE_URL}$bookId/"
        val exclude = arrayOf(ComponentName(requireContext(), MainActivity::class.java))
        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, bookUrl)
            .putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, exclude)
            .setType("text/plain")
            .let { Intent.createChooser(it, null) }
        startActivity(intent)
    }

    private fun onThumbnailClick(pageIndex: Int) {
        goToRead(bookId, pageIndex)
    }

    private fun onTagClick(query: String) {
        activityViewModel.search(query)
        parentFragmentManager.popBackStack()
    }
}