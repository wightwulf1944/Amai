package i.am.shiro.amai.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchJson(
    val result: List<BookJson>,
    val num_pages: Int,
    val per_page: Int
)

@JsonClass(generateAdapter = true)
data class BookJson(
    val id: Int,
    val media_id: String,
    val upload_date: Long,
    val num_favorites: Int,
    val num_pages: Int,
    val scanlator: String,
    val title: TitleJson,
    val tags: List<TagJson>,
    val images: ImagesJson
)

@JsonClass(generateAdapter = true)
data class TitleJson(
    val japanese: String?,
    val pretty: String,
    val english: String
)

@JsonClass(generateAdapter = true)
data class TagJson(
    val id: Int,
    val name: String,
    val type: String,
    val url: String,
    val count: Int
)

@JsonClass(generateAdapter = true)
data class ImagesJson(
    val cover: ImageJson,
    val pages: List<ImageJson>,
    val thumbnail: ImageJson
)

@JsonClass(generateAdapter = true)
data class ImageJson(
    val h: Int,
    val w: Int,
    val t: String
)