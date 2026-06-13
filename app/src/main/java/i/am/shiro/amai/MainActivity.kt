package i.am.shiro.amai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import i.am.shiro.amai.fragment.HomeComposeFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return

        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val bookId = intent.data!!.pathSegments[1].toInt()
                setFragment(HomeComposeFragment(bookId))
            }
            Intent.ACTION_SEND -> {
                try {
                    val bookId = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                        .toUri()
                        .lastPathSegment!!
                        .toInt()
                    setFragment(HomeComposeFragment(bookId))
                } catch (e: Exception) {
                    Timber.e(e)
                    finish()
                }
            }
            else -> {
                setFragment(HomeComposeFragment())
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            add(R.id.fragmentContainer, fragment)
        }
    }
}
