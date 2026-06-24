package i.am.shiro.amai.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// hardcoded thumbnail and image urls may be incorrect. Refer to API docs for correct urls.
// https://nhentai.net/api/v2/docs
object Nhentai {

    const val WEBPAGE_BASE_URL = "https://nhentai.net/g/"

    const val API_BASE_URL = "https://nhentai.net/api/v2/"

    val thumbnailBaseUrl: String
        get() {
            val subdomain = arrayOf("t1", "t2", "t3", "t4").random()
            return "https://$subdomain.nhentai.net/"
        }

    val imageBaseUrl: String
        get() {
            val subdomain = arrayOf("i1", "i2", "i3", "i4").random()
            return "https://$subdomain.nhentai.net/"
        }

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
        ): PaginatedResponse

        @GET("galleries")
        suspend fun getAll(
            @Query("page") page: Int? = null,
            @Query("per_page") perPage: Int? = null
        ): PaginatedResponse

        @GET("galleries/{id}")
        suspend fun getOne(
            @Path("id") id: Int
        ): GalleryDetailResponse
    }
}

@Suppress("PropertyName", "unused")
class PaginatedResponse(
    val result: List<GalleryListItem>,
    val num_pages: Int,
    val per_page: Int,
    val total: Int?
)

@Suppress("PropertyName", "unused")
class GalleryListItem(
    val id: Int,
    val media_id: String,
    val english_title: String,
    val japanese_title: String?,
    val thumbnail: String,
    val thumbnail_width: Int,
    val thumbnail_height: Int,
    val num_pages: Int,
    val tag_ids: List<Int>,
    val blacklisted: Boolean
)

@Suppress("PropertyName", "unused")
class GalleryDetailResponse(
    val id: Int,
    val media_id: String,
    val title: GalleryTitle,
    val cover: CoverInfo,
    val thumbnail: CoverInfo,
    val scanlator: String,
    val upload_date: Long,
    val tags: List<TagResponse>,
    val num_pages: Int,
    val num_favorites: Int,
    val pages: List<PageInfo>
)

@Suppress("unused")
class GalleryTitle(
    val english: String,
    val japanese: String?,
    val pretty: String
)

class CoverInfo(
    val path: String,
    val width: Int,
    val height: Int
)

@Suppress("unused")
class TagResponse(
    val id: Int,
    val type: String,
    val name: String,
    val slug: String,
    val url: String,
    val count: Int
)

@Suppress("PropertyName")
class PageInfo(
    val number: Int,
    val path: String,
    val width: Int,
    val height: Int,
    val thumbnail: String,
    val thumbnail_width: Int,
    val thumbnail_height: Int
)