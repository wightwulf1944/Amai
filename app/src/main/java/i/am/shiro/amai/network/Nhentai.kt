package i.am.shiro.amai.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object Nhentai {

    const val WEBPAGE_BASE_URL = "https://nhentai.net/g/"

    const val THUMBNAIL_BASE_URL = "https://t.nhentai.net/galleries/"

    const val IMAGE_BASE_URL = "https://i.nhentai.net/galleries/"

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