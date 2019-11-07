package i.am.shiro.amai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import i.am.shiro.amai.fragment.LoadingFragment
import i.am.shiro.amai.fragment.MainFragment
import i.am.shiro.amai.fragment.WelcomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        supportFragmentManager.commit {
            add(android.R.id.content, getInitialFragment())
        }
    }

    private fun getInitialFragment() = when {
        intent.action == Intent.ACTION_VIEW -> LoadingFragment()
        Preferences.isFirstRun() -> WelcomeFragment()
        else -> MainFragment()
    }
}
