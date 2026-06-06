package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.SearchScreen
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AmaiTheme {
                    val suggestions by viewModel.suggestionsLive.observeAsState(emptyList())

                    Surface {
                        SearchScreen(
                            suggestions = suggestions,
                            onQueryChange = {
                                viewModel.onQueryChange(it)
                            },
                            onSearch = {
                                activityViewModel.search(it)
                                parentFragmentManager.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
