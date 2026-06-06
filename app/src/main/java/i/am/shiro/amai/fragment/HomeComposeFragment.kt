package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.compose.HomeScreen
import i.am.shiro.amai.util.amaiViewModels
import i.am.shiro.amai.util.goToDetail
import i.am.shiro.amai.util.goToSearch
import i.am.shiro.amai.viewmodel.FavoritesViewModel
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel

class HomeComposeFragment : Fragment() {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val nhentaiViewModel by amaiViewModels<NhentaiViewModel>()
    private val favoritesViewModel by amaiViewModels<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AmaiTheme {
                    HomeScreen(
                        mainViewModel = mainViewModel,
                        nhentaiViewModel = nhentaiViewModel,
                        favoritesViewModel = favoritesViewModel,
                        onSearchClick = { goToSearch() },
                        onItemClick = { bookId -> goToDetail(bookId) }
                    )
                }
            }
        }
    }
}
