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
import i.am.shiro.amai.viewmodel.Home
import i.am.shiro.amai.viewmodel.MainViewModel
import i.am.shiro.amai.viewmodel.Nhentai
import i.am.shiro.amai.viewmodel.Search
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource.Monotonic.markNow

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val activityViewModel by activityViewModels<MainViewModel>()

    private var lastBackPressTime = markNow()

    private lateinit var savedFragment: SavedFragment

    private lateinit var nhentaiFragment: NhentaiFragment

    private lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savedTag = "saved"
        val nhentaiTag = "nhentai"
        val searchTag = "search"

        if (savedInstanceState == null) {
            savedFragment = SavedFragment()
            nhentaiFragment = NhentaiFragment()
            searchFragment = SearchFragment()

            childFragmentManager.commitNow {
                add(R.id.fragmentContainer, savedFragment, savedTag)
                add(R.id.fragmentContainer, nhentaiFragment, nhentaiTag)
                add(R.id.fragmentContainer, searchFragment, searchTag)
                detach(savedFragment)
                detach(searchFragment)
            }
        } else {
            savedFragment = childFragmentManager.findFragmentByTag(savedTag) as SavedFragment
            nhentaiFragment = childFragmentManager.findFragmentByTag(nhentaiTag) as NhentaiFragment
            searchFragment = childFragmentManager.findFragmentByTag(searchTag) as SearchFragment
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
            !savedFragment.isDetached -> R.id.navigation_nhentai
            !nhentaiFragment.isDetached -> R.id.navigation_nhentai
            !searchFragment.isDetached -> R.id.navigation_search
            else -> error(childFragmentManager.fragments)
        }
        b.navigation.setOnItemSelectedListener {
            onNavigate(it.itemId)
            true
        }

        activityViewModel.navigationPathLive.observe(viewLifecycleOwner) { path ->
            path.traverse(Home) { next ->
                when (next) {
                    Nhentai -> b.navigation.selectedItemId = R.id.navigation_nhentai
                    Search -> b.navigation.selectedItemId = R.id.navigation_search
                }
            }
        }
    }

    private fun onNavigate(itemId: Int) {
        childFragmentManager.commit {
            if (itemId == R.id.navigation_saved) attach(savedFragment)
            else detach(savedFragment)

            if (itemId == R.id.navigation_nhentai) attach(nhentaiFragment)
            else detach(nhentaiFragment)

            if (itemId == R.id.navigation_search) attach(searchFragment)
            else detach(searchFragment)

            setReorderingAllowed(true)
        }
    }
}
