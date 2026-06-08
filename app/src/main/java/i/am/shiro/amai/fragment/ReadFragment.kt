package i.am.shiro.amai.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.ReadScreen
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.viewmodel.ReadViewModel

class ReadFragment() : Fragment() {

    private val viewModel by amaiViewModels<ReadViewModel>()

    private var bookId by argument<Int>()

    private var pageIndex by argument<Int>()

    constructor(bookId: Int, pageIndex: Int) : this() {
        this.bookId = bookId
        this.pageIndex = pageIndex
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val window = requireActivity().window
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDetach() {
        super.onDetach()

        val window = requireActivity().window
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.statusBars())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setBookId(bookId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AmaiTheme {
                    ReadScreen(
                        viewModel = viewModel,
                        initialPage = pageIndex
                    )
                }
            }
        }
    }
}
