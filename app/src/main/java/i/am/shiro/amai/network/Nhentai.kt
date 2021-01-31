package i.am.shiro.amai.network

import i.am.shiro.amai.BuildConfig
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object Nhentai {

    private const val API_URL = "https://nhentai.net/api/"

    const val WEBPAGE_BASE_URL = "https://nhentai.net/g/"

    const val THUMBNAIL_BASE_URL = "https://t.nhentai.net/galleries/"

    const val IMAGE_BASE_URL = "https://i.nhentai.net/galleries/"

    val API: Api = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(UserAgentInterceptor())
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(if (BuildConfig.DEBUG) BODY else NONE)
                )
                .build()
        )
        .baseUrl(API_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

    interface Api {

        @GET("galleries/search")
        fun search(
            @Query("query") query: String?,
            @Query("page") page: Int?,
            @Query("sort") sort: Sort?
        ): Single<SearchJson>

        @GET("galleries/all")
        fun getAll(@Query("page") page: Int?): Single<SearchJson>

        @GET("gallery/{id}")
        fun getBook(@Path("id") id: Int): Single<BookJson>

        @GET("gallery/{id}/related")
        fun getRelatedBooks(@Path("id") id: Int): Single<BookJson>

        @GET("galleries/tagged")
        fun getTagById(
            @Query("tag_id") tagId: Int?,
            @Query("page") page: Int?,
            @Query("sort") sort: Sort?
        ): Single<SearchJson>
    }

    enum class Sort(private val s: String) {
        POPULAR("popular"),
        DATE("date");

        override fun toString() = s
    }
}