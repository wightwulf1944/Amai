package i.am.shiro.amai

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.content.getSystemService
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import i.am.shiro.amai.koin.mainModule
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AmaiApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) initDebugTools()

        startKoin {
            androidContext(this@AmaiApplication)
            modules(mainModule)
        }

        val mChannel = NotificationChannel(
            DEFAULT_CHANNEL_ID,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT)

        getSystemService<NotificationManager>()!!.createNotificationChannel(mChannel)
    }

    private fun initDebugTools() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                val tag = super.createStackElementTag(element)
                val method = element.methodName
                return String.format("%s:%s", tag, method)
            }
        })
    }

    override fun newImageLoader() = ImageLoader.Builder(this)
        .logger(DebugLogger())
        .okHttpClient(get<OkHttpClient>())
        .build()
}