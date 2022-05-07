package i.am.shiro.amai.koin

import androidx.preference.PreferenceManager
import androidx.room.Room
import coil.util.CoilUtils
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.data.AmaiDatabase
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    single {
        Room.databaseBuilder(androidContext(), AmaiDatabase::class.java, "amai")
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }
    single {
        AmaiPreferences(PreferenceManager.getDefaultSharedPreferences(androidContext()))
    }
    single {
        OkHttpClient.Builder()
            .cache(CoilUtils.createDefaultCache(androidContext()))
            .build()
    }
}