package i.am.shiro.amai.data.repository

import androidx.room.withTransaction
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.FavoriteEntity
import i.am.shiro.amai.data.local.entity.ImageEntity
import i.am.shiro.amai.data.local.entity.TagEntity
import i.am.shiro.amai.data.local.intermediate.DetailIntermediate
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.remote.dto.GalleryDetailDto
import i.am.shiro.amai.model.BookDetail
import i.am.shiro.amai.model.Tag
import i.am.shiro.amai.model.Thumbnail
import kotlinx.coroutines.flow.map

class BookRepository(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) {
    fun getBookDetail(bookId: Int) = database.intermediateDao.getDetail(bookId)
        .map { it?.toBookDetail() }

    suspend fun refreshBookDetail(bookId: Int) {
        val detailedBookJson = nhentaiApi.getOne(bookId)
        database.withTransaction {
            database.bookDao.insert(detailedBookJson.toEntity())
            database.tagDao.insert(detailedBookJson.tagEntities())
            database.imageDao.insert(detailedBookJson.imageEntities())
        }
    }

    suspend fun toggleFavorite(bookId: Int, isFavorite: Boolean) {
        if (isFavorite) {
            database.favoriteDao.insert(FavoriteEntity(bookId))
        } else {
            database.favoriteDao.deleteById(bookId)
        }
    }

    private fun DetailIntermediate.toBookDetail(): BookDetail {

        val tagMap = tagEntities.groupBy(TagEntity::type) {
            val searchTag = if (it.name.any(Char::isWhitespace)) "\"${it.name}\"" else it.name
            Tag(it.id, it.name, "${it.type}:$searchTag")
        }

        val thumbnails = remoteImageEntities.map {
            val width = it.thumbnailWidth.coerceAtLeast(1).toFloat()
            val height = it.thumbnailHeight.coerceAtLeast(1).toFloat()
            Thumbnail(
                aspectRatio = width / height,
                path = it.thumbnailPath
            )
        }

        return BookDetail(
            title = bookEntity.title,
            pageCount = bookEntity.pageCount,
            uploadDate = bookEntity.uploadDate,
            isFavorite = favoriteEntity != null,
            artistTags = tagMap["artist"],
            groupTags = tagMap["group"],
            parodyTags = tagMap["parody"],
            characterTags = tagMap["character"],
            languageTags = tagMap["language"],
            categoryTags = tagMap["category"],
            generalTags = tagMap["tag"],
            thumbnails = thumbnails
        )
    }

    private fun GalleryDetailDto.toEntity() =
        BookEntity(
            bookId = id,
            title = title.english,
            pageCount = num_pages,
            thumbnailWidth = thumbnail.width,
            thumbnailHeight = thumbnail.height,
            thumbnailPath = thumbnail.path,
            uploadDate = upload_date,
        )

    private fun GalleryDetailDto.tagEntities(): List<TagEntity> = tags.map {
        TagEntity(
            id = it.id,
            bookId = id,
            name = it.name,
            type = it.type
        )
    }

    private fun GalleryDetailDto.imageEntities(): List<ImageEntity> = pages.map { page ->
        ImageEntity(
            bookId = id,
            pageIndex = page.number,
            width = page.width,
            height = page.height,
            path = page.path,
            thumbnailWidth = page.thumbnail_width,
            thumbnailHeight = page.thumbnail_height,
            thumbnailPath = page.thumbnail
        )
    }
}
