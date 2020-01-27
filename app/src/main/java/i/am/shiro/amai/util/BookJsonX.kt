package i.am.shiro.amai.util

import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.RemoteImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.dto.BookJson
import i.am.shiro.amai.network.dto.ImageJson

fun BookJson.toEntity() = BookEntity(
    bookId = id,
    webUrl = Nhentai.WEBPAGE_BASE_URL + id,
    title = title.pretty,
    pageCount = pageCount,
    favCount = favCount,
    uploadDate = uploadDate
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
        width = page.width,
        height = page.height,
        url = "${Nhentai.IMAGE_BASE_URL}$mediaId/${index + 1}${page.extension()}",
        thumbnailWidth = page.width, // TODO this is actually wrong
        thumbnailHeight = page.height, // TODO this is actually wrong
        thumbnailUrl = "${Nhentai.THUMBNAIL_BASE_URL}$mediaId/${index + 1}t${page.extension()}"
    )
}

private fun ImageJson.extension() = when (type) {
    "j" -> ".jpg"
    "p" -> ".png"
    "g" -> ".gif"
    else -> throw RuntimeException("Unknown type $type")
}