package i.am.shiro.amai.util

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.show(dialog: DialogFragment) {
    dialog.show(this, null)
}