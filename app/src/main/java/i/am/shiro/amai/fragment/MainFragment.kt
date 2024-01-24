package i.am.shiro.amai.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.google.android.material.snackbar.Snackbar
import i.am.shiro.amai.R
import i.am.shiro.amai.RESULT_TAG
import i.am.shiro.amai.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private var lastBackPressTime = 0L

    private lateinit var savedFragment: SavedFragment

    private lateinit var nhentaiFragment: Nhentai2Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savedTag = "saved"
        val nhentaiTag = "nhentai"

        if (savedInstanceState == null) {
            savedFragment = SavedFragment()
            nhentaiFragment = Nhentai2Fragment()

            childFragmentManager.commitNow {
                add(R.id.fragmentContainer, savedFragment, savedTag)
                add(R.id.fragmentContainer, nhentaiFragment, nhentaiTag)
                detach(savedFragment)
            }
        } else {
            savedFragment = childFragmentManager.findFragmentByTag(savedTag) as SavedFragment
            nhentaiFragment = childFragmentManager.findFragmentByTag(nhentaiTag) as Nhentai2Fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentMainBinding.bind(view)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val now = SystemClock.elapsedRealtime()
            if (now > lastBackPressTime + 1000) {
                lastBackPressTime = now
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
            else -> error(childFragmentManager.fragments)
        }
        b.navigation.setOnItemSelectedListener {
            onNavigate(it.itemId)
            true
        }

        parentFragmentManager.setFragmentResultListener(RESULT_TAG, viewLifecycleOwner) { _, result ->
            b.navigation.selectedItemId = R.id.navigation_nhentai
            childFragmentManager.setFragmentResult(RESULT_TAG, result)
        }
    }

    private fun onNavigate(itemId: Int) {
        val targetFragment = when (itemId) {
            R.id.navigation_saved -> savedFragment
            R.id.navigation_nhentai -> nhentaiFragment
            else -> throw IllegalArgumentException()
        }

        childFragmentManager.commit {
            for (fragment in arrayOf(savedFragment, nhentaiFragment)) {
                if (fragment == targetFragment) attach(fragment) else detach(fragment)
            }
            setReorderingAllowed(true)
        }
    }
}
