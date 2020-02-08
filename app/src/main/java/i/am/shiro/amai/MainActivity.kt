package i.am.shiro.amai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import i.am.shiro.amai.util.startAtLoading
import i.am.shiro.amai.util.startAtMain
import i.am.shiro.amai.util.startAtWelcome

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        when {
            intent.action == Intent.ACTION_VIEW -> {
                startAtLoading(intent.data!!.pathSegments[1].toInt())
            }
            Preferences.isFirstRun() -> {
                startAtWelcome()
            }
            else -> {
                startAtMain()
            }
        }
    }
}
