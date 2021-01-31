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
import kotlinx.android.synthetic.main.fragment_main.*

private const val SAVED = "saved"
private const val NHENTAI = "nhentai"
private const val DOWNLOADS = "downloads"

class MainFragment : Fragment(R.layout.fragment_main) {

    private var lastBackPressTime = 0L

    private lateinit var savedFragment: SavedFragment

    private lateinit var nhentaiFragment: NhentaiFragment

    private lateinit var downloadsFragment: DownloadsFragment

    private var isNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            savedFragment = SavedFragment()
            nhentaiFragment = NhentaiFragment()
            downloadsFragment = DownloadsFragment()

            childFragmentManager.commitNow {
                add(R.id.fragmentContainer, savedFragment, SAVED)
                add(R.id.fragmentContainer, nhentaiFragment, NHENTAI)
                add(R.id.fragmentContainer, downloadsFragment, DOWNLOADS)
                detach(savedFragment)
                detach(downloadsFragment)
            }

            isNew = true
        } else {
            savedFragment = childFragmentManager.findFragmentByTag(SAVED) as SavedFragment
            nhentaiFragment = childFragmentManager.findFragmentByTag(NHENTAI) as NhentaiFragment
            downloadsFragment = childFragmentManager.findFragmentByTag(DOWNLOADS) as DownloadsFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val now = SystemClock.elapsedRealtime()
            if (now > lastBackPressTime + 1000) {
                lastBackPressTime = now
                Snackbar.make(view, R.string.confirm_exit, Snackbar.LENGTH_SHORT)
                    .setAnchorView(navigation)
                    .show()
            } else {
                requireActivity().finish()
            }
        }

        if (isNew) navigation.selectedItemId = R.id.navigation_nhentai
        navigation.setOnNavigationItemSelectedListener {
            onNavigate(it.itemId)
            true
        }
    }

    private fun onNavigate(itemId: Int) {
        val targetFragment = when (itemId) {
            R.id.navigation_saved -> savedFragment
            R.id.navigation_nhentai -> nhentaiFragment
            R.id.navigation_downloads -> downloadsFragment
            else -> throw IllegalArgumentException()
        }

        childFragmentManager.commit {
            for (fragment in arrayOf(savedFragment, nhentaiFragment, downloadsFragment)) {
                if (fragment == targetFragment) attach(fragment) else detach(fragment)
            }
            setReorderingAllowed(true)
        }
    }
}
