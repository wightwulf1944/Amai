package i.am.shiro.amai.koin

import androidx.preference.PreferenceManager
import androidx.room.Room
import coil.util.CoilUtils
import com.squareup.moshi.Moshi
import i.am.shiro.amai.API_URL
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.BuildConfig
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.UserAgentInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

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
            .addInterceptor(UserAgentInterceptor())
            .build()
    }
    single<Nhentai.Api> {
        Retrofit.Builder()
            .client(
                get<OkHttpClient>()
                    .newBuilder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
                    )
                    .build()
            )
            .baseUrl(API_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}