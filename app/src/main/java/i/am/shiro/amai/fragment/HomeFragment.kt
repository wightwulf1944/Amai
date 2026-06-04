package i.am.shiro.amai.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.google.android.material.snackbar.Snackbar
import i.am.shiro.amai.R
import i.am.shiro.amai.databinding.FragmentHomeBinding
import i.am.shiro.amai.viewmodel.MainViewModel
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource.Monotonic.markNow

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val activityViewModel by activityViewModels<MainViewModel>()

    private var lastBackPressTime = markNow()

    private lateinit var favoritesFragment: FavoritesFragment

    private lateinit var nhentaiFragment: NhentaiFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val favoritesTag = "favorites"
        val nhentaiTag = "nhentai"

        if (savedInstanceState == null) {
            favoritesFragment = FavoritesFragment()
            nhentaiFragment = NhentaiFragment()

            childFragmentManager.commitNow {
                add(R.id.fragmentContainer, favoritesFragment, favoritesTag)
                add(R.id.fragmentContainer, nhentaiFragment, nhentaiTag)
                detach(favoritesFragment)
            }
        } else {
            favoritesFragment = childFragmentManager.findFragmentByTag(favoritesTag) as FavoritesFragment
            nhentaiFragment = childFragmentManager.findFragmentByTag(nhentaiTag) as NhentaiFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentHomeBinding.bind(view)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (lastBackPressTime.elapsedNow() > 1.seconds) {
                lastBackPressTime = markNow()
                // TODO replace this with actual view in layout
                Snackbar.make(view, R.string.confirm_exit, Snackbar.LENGTH_SHORT)
                    .setAnchorView(b.navigation)
                    .show()
            } else {
                requireActivity().finish()
            }
        }

        b.navigation.selectedItemId = when {
            !favoritesFragment.isDetached -> R.id.navigation_favorites
            !nhentaiFragment.isDetached -> R.id.navigation_nhentai
            else -> error(childFragmentManager.fragments)
        }
        b.navigation.setOnItemSelectedListener {
            onNavigate(it.itemId)
            true
        }

        activityViewModel.searchEventLive.observe(viewLifecycleOwner) { event ->
            if (!event.isHomeConsumed) {
                b.navigation.selectedItemId = R.id.navigation_nhentai
                event.isHomeConsumed = true
            }
        }
    }

    private fun onNavigate(itemId: Int) {
        childFragmentManager.commit {
            if (itemId == R.id.navigation_favorites) attach(favoritesFragment)
            else detach(favoritesFragment)

            if (itemId == R.id.navigation_nhentai) attach(nhentaiFragment)
            else detach(nhentaiFragment)

            setReorderingAllowed(true)
        }
    }
}
