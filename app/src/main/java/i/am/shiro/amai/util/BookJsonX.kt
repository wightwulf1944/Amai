package i.am.shiro.amai.util

import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.RemoteImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.BookJson
import i.am.shiro.amai.network.ImageJson
import kotlin.math.min

fun BookJson.toEntity() = BookEntity(
    bookId = id,
    webUrl = Nhentai.WEBPAGE_BASE_URL + id,
    title = title.pretty,
    pageCount = num_pages,
    favCount = num_favorites,
    uploadDate = upload_date
)

fun BookJson.tagEntities(): List<TagEntity> = tags.map {
    TagEntity(
        bookId = id,
        name = it.name,
        type = it.type
    )
}

fun BookJson.imageEntities(): List<RemoteImageEntity> = images.pages.mapIndexed { index, page ->
    RemoteImageEntity(
        bookId = id,
        pageIndex = index,
        width = page.w,
        height = page.h,
        url = "${Nhentai.IMAGE_BASE_URL}$media_id/${index + 1}${page.extension()}",
        thumbnailWidth = page.w,
        thumbnailHeight = min(page.h, (page.w / 200.0 * 364.0).toInt()),
        thumbnailUrl = "${Nhentai.THUMBNAIL_BASE_URL}$media_id/${index + 1}t${page.extension()}"
    )
}

private fun ImageJson.extension() = when (t) {
    "j" -> ".jpg"
    "p" -> ".png"
    "g" -> ".gif"
    else -> throw RuntimeException("Unknown type $t")
}