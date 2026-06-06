package i.am.shiro.amai.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import i.am.shiro.amai.MainActivity
import i.am.shiro.amai.R
import i.am.shiro.amai.fragment.*

fun MainActivity.startAtDetail(bookId: Int) {
    setFragment(DetailFragment(bookId))
}

fun MainActivity.startAtWelcome() {
    setFragment(InitialSetupFragment())
}

fun MainActivity.startAtHome() {
    setFragment(HomeComposeFragment())
}

fun DetailFragment.goToRead(bookId: Int, pageIndex: Int) {
    pushFragment(ReadFragment(bookId, pageIndex))
}

fun InitialSetupFragment.goToMain() {
    replaceFragment(HomeComposeFragment())
}

fun Fragment.goToDetail(bookId: Int) {
    pushFragment(DetailFragment(bookId))
}

fun Fragment.goToSearch() {
    pushFragment(SearchFragment())
}

private fun FragmentActivity.setFragment(fragment: Fragment) {
    supportFragmentManager.commit {
        add(R.id.fragmentContainer, fragment)
    }
}

private fun Fragment.replaceFragment(fragment: Fragment) {
    requireActivity().supportFragmentManager.commit {
        replace(R.id.fragmentContainer, fragment)
    }
}

private fun Fragment.pushFragment(fragment: Fragment) {
    requireActivity().supportFragmentManager.commit {
        replace(R.id.fragmentContainer, fragment)
        addToBackStack(null)
    }
}

