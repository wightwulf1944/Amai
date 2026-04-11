package i.am.shiro.amai.util

import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.GalleryListItem
import i.am.shiro.amai.network.Nhentai.IMAGE_BASE_URL
import i.am.shiro.amai.network.Nhentai.THUMBNAIL_BASE_URL

fun GalleryDetailResponse.toEntity() = BookEntity(
    bookId = id,
    title = title.english,
    pageCount = num_pages,
    thumbnailWidth = thumbnail.width,
    thumbnailHeight = thumbnail.height,
    thumbnailUrl = THUMBNAIL_BASE_URL + thumbnail.path
)

fun GalleryListItem.toEntity() = BookEntity(
    bookId = id,
    title = english_title,
    pageCount = num_pages,
    thumbnailWidth = thumbnail_width,
    thumbnailHeight = thumbnail_height,
    thumbnailUrl = THUMBNAIL_BASE_URL + thumbnail
)

fun GalleryDetailResponse.tagEntities(): List<TagEntity> = tags.map {
    TagEntity(
        bookId = id,
        name = it.name,
        type = it.type
    )
}

fun GalleryDetailResponse.imageEntities(): List<ImageEntity> = pages.map { page ->
    ImageEntity(
        bookId = id,
        pageIndex = page.number,
        width = page.width,
        height = page.height,
        url = IMAGE_BASE_URL + page.path,
        thumbnailWidth = page.thumbnail_width,
        thumbnailHeight = page.thumbnail_height,
        thumbnailUrl = THUMBNAIL_BASE_URL + page.thumbnail
    )
}
