package i.am.shiro.amai.util

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

// todo replace this with FragmentManager.show(DialogFragment)
fun DialogFragment.show(fragmentManager: FragmentManager) {
    show(fragmentManager, null)
}