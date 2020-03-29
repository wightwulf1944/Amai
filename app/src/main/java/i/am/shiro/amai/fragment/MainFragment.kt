package i.am.shiro.amai.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import i.am.shiro.amai.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private var lastBackPressTime: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestFocus()
        view.setOnBackKeyListener(::onBackPress)

        navigation.setOnNavigateListener(::onNavigate)

        if (childFragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            navigation.selectedItemId = R.id.navigation_nhentai
        }
    }

    private fun onBackPress() {
        val now = SystemClock.elapsedRealtime()
        if (now > lastBackPressTime + 1000) {
            lastBackPressTime = now
            Snackbar.make(requireView(), R.string.confirm_exit, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.navigation)
                .show()
        } else {
            requireActivity().finish()
        }
    }

    private fun onNavigate(itemId: Int) {
        val fragmentTransaction = childFragmentManager.beginTransaction()

        // detach previous fragment if any
        val detachingFragment = childFragmentManager.primaryNavigationFragment
        if (detachingFragment != null) {
            fragmentTransaction.detach(detachingFragment)
        }

        val fragmentTag = getFragmentTag(itemId)
        var attachingFragment = childFragmentManager.findFragmentByTag(fragmentTag)
        if (attachingFragment == null) { // add fragment if it has not been added
            attachingFragment = getFragment(itemId)
            fragmentTransaction.add(R.id.fragmentContainer, attachingFragment, fragmentTag)
        } else { // attach fragment if it has already been added
            fragmentTransaction.attach(attachingFragment)
        }
        fragmentTransaction.setPrimaryNavigationFragment(attachingFragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commit()
    }

    private fun getFragmentTag(@IdRes itemId: Int): String = when (itemId) {
        R.id.navigation_saved -> SavedFragment::class.java.name
        R.id.navigation_nhentai -> NhentaiFragment::class.java.name
        R.id.navigation_downloads -> DownloadsFragment::class.java.name
        else -> throw IllegalArgumentException("No corresponding fragment for given itemId")
    }

    private fun getFragment(@IdRes itemId: Int): Fragment = when (itemId) {
        R.id.navigation_saved -> SavedFragment()
        R.id.navigation_nhentai -> NhentaiFragment()
        R.id.navigation_downloads -> DownloadsFragment()
        else -> throw IllegalArgumentException("No corresponding fragment for given itemId")
    }
}

private fun BottomNavigationView.setOnNavigateListener(listener: (Int) -> Unit) =
    setOnNavigationItemSelectedListener {
        listener(it.itemId)
        true
    }

private fun View.setOnBackKeyListener(listener: () -> Unit) {
    setOnKeyListener { _, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_BACK &&
            event.action == KeyEvent.ACTION_DOWN &&
            event.repeatCount == 0) {
            listener()
            true
        } else {
            false
        }
    }
}