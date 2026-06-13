package i.am.shiro.amai.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import i.am.shiro.amai.MainActivity
import i.am.shiro.amai.R
import i.am.shiro.amai.fragment.HomeComposeFragment

fun MainActivity.startAtDetail(bookId: Int) {
    setFragment(HomeComposeFragment(bookId))
}

fun MainActivity.startAtHome() {
    setFragment(HomeComposeFragment())
}

private fun FragmentActivity.setFragment(fragment: Fragment) {
    supportFragmentManager.commit {
        add(R.id.fragmentContainer, fragment)
    }
}

