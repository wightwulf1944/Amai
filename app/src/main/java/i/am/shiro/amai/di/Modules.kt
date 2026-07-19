package i.am.shiro.amai.di

import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import i.am.shiro.amai.BuildConfig
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.remote.UserAgentInterceptor
import i.am.shiro.amai.data.repository.BookRepository
import i.am.shiro.amai.data.repository.FavoritesRepository
import i.am.shiro.amai.data.repository.GalleryRepository
import i.am.shiro.amai.data.repository.PagingGalleryRepository
import i.am.shiro.amai.data.repository.ReadRepository
import i.am.shiro.amai.data.repository.SearchRepository
import i.am.shiro.amai.ui.viewmodel.DetailViewModel
import i.am.shiro.amai.ui.viewmodel.FavoritesViewModel
import i.am.shiro.amai.ui.viewmodel.NhentaiLatestViewModel
import i.am.shiro.amai.ui.viewmodel.NhentaiSearchViewModel
import i.am.shiro.amai.ui.viewmodel.NhentaiTagViewModel
import i.am.shiro.amai.ui.viewmodel.ReadViewModel
import i.am.shiro.amai.ui.viewmodel.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

val mainModule = module {
    single {
        Room.databaseBuilder(androidContext(), AmaiDatabase::class.java, "amai")
            .addMigrations(AmaiDatabase.MIGRATION_32_33)
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
                            .setLevel(if (BuildConfig.DEBUG) Level.BODY else Level.NONE)
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
    single { PagingGalleryRepository(get(), get()) }
    single { ReadRepository(get()) }
    single { FavoritesRepository(get()) }
    single { SearchRepository() }

    viewModelOf(::SearchViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::NhentaiLatestViewModel)
    viewModelOf(::NhentaiSearchViewModel)
    viewModelOf(::NhentaiTagViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::ReadViewModel)
}
