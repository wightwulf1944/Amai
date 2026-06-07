package i.am.shiro.amai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import i.am.shiro.amai.util.startAtDetail
import i.am.shiro.amai.util.startAtHome
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return

        when (intent.action) {
            Intent.ACTION_VIEW -> {
                startAtDetail(intent.data!!.pathSegments[1].toInt())
            }
            Intent.ACTION_SEND -> {
                try {
                    val bookId = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                        .toUri()
                        .lastPathSegment!!
                        .toInt()
                    startAtDetail(bookId)
                } catch (e: Exception) {
                    Timber.e(e)
                    finish()
                }
            }
            else -> {
                startAtHome()
            }
        }
    }
}
