package i.am.shiro.amai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import i.am.shiro.amai.fragment.MainFragment
import i.am.shiro.amai.fragment.WelcomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val initialFragment = if (Preferences.isFirstRun()) {
                WelcomeFragment()
            } else {
                MainFragment()
            }

            supportFragmentManager.commit {
                add(android.R.id.content, initialFragment)
            }
        }
    }
}
