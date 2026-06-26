package i.am.shiro.amai.koin

import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import i.am.shiro.amai.BuildConfig
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.UserAgentInterceptor
import i.am.shiro.amai.repository.BookRepository
import i.am.shiro.amai.repository.FavoritesRepository
import i.am.shiro.amai.repository.GalleryRepository
import i.am.shiro.amai.repository.ReadRepository
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.FavoritesViewModel
import i.am.shiro.amai.viewmodel.HomepageViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.ReadViewModel
import i.am.shiro.amai.viewmodel.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

// TODO use koin compiler for compiletime safety
val mainModule = module {
    single {
        Room.databaseBuilder(androidContext(), AmaiDatabase::class.java, "amai")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor())
            .build()
    }
    single<Nhentai.Api> {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

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
            .baseUrl(Nhentai.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    single { BookRepository(get(), get()) }
    single { GalleryRepository(get(), get()) }
    single { ReadRepository(get()) }
    single { FavoritesRepository(get()) }

    viewModelOf(::SearchViewModel)
    viewModelOf(::NhentaiViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::ReadViewModel)
    viewModelOf(::HomepageViewModel)
}
