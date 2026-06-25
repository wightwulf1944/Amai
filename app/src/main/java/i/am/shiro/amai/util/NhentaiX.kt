package i.am.shiro.amai.util

import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.GalleryListItem

fun GalleryDetailResponse.toEntity() = BookEntity(
    bookId = id,
    title = title.english,
    pageCount = num_pages,
    thumbnailWidth = thumbnail.width,
    thumbnailHeight = thumbnail.height,
    thumbnailUrl = thumbnail.path
)

fun GalleryListItem.toEntity() = BookEntity(
    bookId = id,
    title = english_title,
    pageCount = num_pages,
    thumbnailWidth = thumbnail_width,
    thumbnailHeight = thumbnail_height,
    thumbnailUrl = thumbnail
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
        url = page.path,
        thumbnailWidth = page.thumbnail_width,
        thumbnailHeight = page.thumbnail_height,
        thumbnailUrl = page.thumbnail
    )
}
