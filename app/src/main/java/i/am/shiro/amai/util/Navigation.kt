package i.am.shiro.amai.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import i.am.shiro.amai.MainActivity
import i.am.shiro.amai.R
import i.am.shiro.amai.fragment.*

fun MainActivity.startAtLoading(bookId: Int) {
    setFragment(LoadingFragment(bookId))
}

fun MainActivity.startAtWelcome() {
    setFragment(WelcomeFragment())
}

fun MainActivity.startAtMain() {
    setFragment(MainFragment())
}

fun LoadingFragment.goToDetail(bookId: Int) {
    replaceFragment(DetailFragment(bookId))
}

fun DetailFragment.goToRead(bookId: Int, pageIndex: Int) {
    pushFragment(ReadFragment(bookId, pageIndex))
}

fun WelcomeFragment.goToMain() {
    replaceFragment(MainFragment())
}

fun WelcomeFragment.goToStorageSetup() {
    pushFragment(StorageSetupFragment())
}

fun StorageSetupFragment.goToMain() {
    popFragment()
    replaceFragment(MainFragment())
}

fun SavedFragment.goToDetail(bookId: Int) {
    pushFragment(DetailFragment(bookId))
}

fun NhentaiFragment.goToDetail(bookId: Int) {
    pushFragment(DetailFragment(bookId))
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

private fun Fragment.popFragment() {
    requireActivity().supportFragmentManager.popBackStack()
}
