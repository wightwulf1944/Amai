package i.am.shiro.amai.data.remote

import i.am.shiro.amai.data.remote.dto.GalleryDetailDto
import i.am.shiro.amai.data.remote.dto.PaginatedDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// hardcoded thumbnail and image urls may be incorrect. Refer to API docs for correct urls.
// https://nhentai.net/api/v2/docs
object Nhentai {

    const val WEBPAGE_BASE_URL = "https://nhentai.net/g/"

    const val API_BASE_URL = "https://nhentai.net/api/v2/"

    val thumbnailBaseUrls = arrayOf(
        "https://t1.nhentai.net/",
        "https://t2.nhentai.net/",
        "https://t3.nhentai.net/",
        "https://t4.nhentai.net/"
    )

    val imageBaseUrls = arrayOf(
        "https://i1.nhentai.net/",
        "https://i2.nhentai.net/",
        "https://i3.nhentai.net/",
        "https://i4.nhentai.net/"
    )

    enum class Sort(private val s: String) {
        DATE("date"),
        POPULAR("popular"),
        POPULAR_TODAY("popular-today"),
        POPULAR_WEEK("popular-week"),
        POPULAR_MONTH("popular-month");

        override fun toString() = s
    }

    interface Api {

        @GET("search")
        suspend fun search(
            @Query("query") query: String,
            @Query("sort") sort: Sort?,
            @Query("page") page: Int?
        ): PaginatedDto

        @GET("galleries")
        suspend fun getAll(
            @Query("page") page: Int? = null,
            @Query("per_page") perPage: Int? = null
        ): PaginatedDto

        @GET("galleries/{id}")
        suspend fun getOne(
            @Path("id") id: Int
        ): GalleryDetailDto
    }
}
