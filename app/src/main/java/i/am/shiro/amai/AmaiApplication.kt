package i.am.shiro.amai

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import i.am.shiro.amai.constant.Constants.DEFAULT_CHANNEL_ID
import i.am.shiro.amai.data.AmaiDatabase
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

lateinit var DATABASE: AmaiDatabase
    private set

class AmaiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Preferences.init(this)

        if (BuildConfig.DEBUG) initDebugTools()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .migration(Migration())
            .compactOnLaunch()
            .build()
        Realm.setDefaultConfiguration(config)

        DATABASE = Room.databaseBuilder(this, AmaiDatabase::class.java, "books")
            .fallbackToDestructiveMigration()
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                DEFAULT_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT)

            getSystemService(NotificationManager::class.java).createNotificationChannel(mChannel)
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

        val glideBuilder = GlideBuilder()
            .setLogLevel(Log.VERBOSE)
        Glide.init(this, glideBuilder)
    }
}
