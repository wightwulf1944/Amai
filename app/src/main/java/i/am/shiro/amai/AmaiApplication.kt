package i.am.shiro.amai

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.util.DebugLogger
import i.am.shiro.amai.coil3.PageInterceptor
import i.am.shiro.amai.coil3.ThumbnailInterceptor
import i.am.shiro.amai.koin.mainModule
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AmaiApplication : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) initDebugTools()

        startKoin {
            androidContext(this@AmaiApplication)
            modules(mainModule)
        }
    }

    private fun initDebugTools() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                val tag = super.createStackElementTag(element)
                val method = element.methodName
                return "$tag:$method"
            }
        })
    }

    override fun newImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
        .components {
            add(ThumbnailInterceptor())
            add(PageInterceptor())
            add(OkHttpNetworkFetcherFactory(get<OkHttpClient>()))
        }
        .logger(DebugLogger())
        .build()
}