package i.am.shiro.amai

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.facebook.stetho.Stetho
import i.am.shiro.amai.constant.Constants.DEFAULT_CHANNEL_ID
import i.am.shiro.amai.dagger.AmaiComponent
import i.am.shiro.amai.dagger.DaggerAmaiComponent
import timber.log.Timber

class AmaiApplication : Application() {

    val component: AmaiComponent by lazy {
        DaggerAmaiComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) initDebugTools()

        Preferences.init(this)

        Stetho.initializeWithDefaults(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                DEFAULT_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT)

            getSystemService<NotificationManager>()!!.createNotificationChannel(mChannel)
        }
    }

    private fun initDebugTools() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                val tag = super.createStackElementTag(element)
                val method = element.methodName
                return String.format("%s:%s", tag, method)
            }
        })

        val glideBuilder = GlideBuilder().setLogLevel(Log.VERBOSE)
        Glide.init(this, glideBuilder)
    }
}