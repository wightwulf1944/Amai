package i.am.shiro.amai.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import i.am.shiro.amai.MainActivity
import i.am.shiro.amai.R
import i.am.shiro.amai.fragment.HomeComposeFragment
import i.am.shiro.amai.fragment.ReadFragment

fun MainActivity.startAtDetail(bookId: Int) {
    setFragment(HomeComposeFragment(bookId))
}

fun MainActivity.startAtHome() {
    setFragment(HomeComposeFragment())
}

fun HomeComposeFragment.goToRead(bookId: Int, pageIndex: Int) {
    pushFragment(ReadFragment(bookId, pageIndex))
}

private fun FragmentActivity.setFragment(fragment: Fragment) {
    supportFragmentManager.commit {
        add(R.id.fragmentContainer, fragment)
    }
}

private fun Fragment.pushFragment(fragment: Fragment) {
    requireActivity().supportFragmentManager.commit {
        replace(R.id.fragmentContainer, fragment)
        addToBackStack(null)
    }
}

