package i.am.shiro.amai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import i.am.shiro.amai.util.startAtLoading
import i.am.shiro.amai.util.startAtMain
import i.am.shiro.amai.util.startAtWelcome
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val preferences by inject<AmaiPreferences>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return

        when {
            intent.action == Intent.ACTION_VIEW -> {
                startAtLoading(intent.data!!.pathSegments[1].toInt())
            }
            preferences.isFirstRun -> {
                startAtWelcome()
            }
            else -> {
                startAtMain()
            }
        }
    }
}
