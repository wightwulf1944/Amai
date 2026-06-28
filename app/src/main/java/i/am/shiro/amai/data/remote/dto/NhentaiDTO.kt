@file:Suppress("PropertyName", "unused")

package i.am.shiro.amai.data.remote.dto

// PaginatedResponse in the swagger docs.
class PaginatedDto(
    val result: List<GalleryListItemDto>,
    val num_pages: Int,
    val per_page: Int,
    val total: Int?
)

// GalleryListItem in the swagger docs.
class GalleryListItemDto(
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

// GalleryDetailResponse in the swagger docs.
class GalleryDetailDto(
    val id: Int,
    val media_id: String,
    val title: GalleryTitleDto,
    val cover: CoverDto,
    val thumbnail: CoverDto,
    val scanlator: String,
    val upload_date: Long,
    val tags: List<TagDto>,
    val num_pages: Int,
    val num_favorites: Int,
    val pages: List<PageDto>
)

// GalleryTitle in the swagger docs.
class GalleryTitleDto(
    val english: String,
    val japanese: String?,
    val pretty: String
)

// CoverInfo in the swagger docs.
class CoverDto(
    val path: String,
    val width: Int,
    val height: Int
)

// TagResponse in the swagger docs.
class TagDto(
    val id: Int,
    val type: String,
    val name: String,
    val slug: String,
    val url: String,
    val count: Int
)

// PageInfo in the swagger docs.
class PageDto(
    val number: Int,
    val path: String,
    val width: Int,
    val height: Int,
    val thumbnail: String,
    val thumbnail_width: Int,
    val thumbnail_height: Int
)
