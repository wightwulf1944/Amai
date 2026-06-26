package i.am.shiro.amai.repository

import androidx.room.withTransaction
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.FavoriteEntity
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.intermediate.DetailIntermediate
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.TagModel
import i.am.shiro.amai.model.Thumbnail
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.Nhentai
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepository(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) {
    fun getBookDetail(bookId: Int): Flow<DetailModel?> {
        return database.intermediateDao.getDetail(bookId)
            .map { it?.toDetailModel() }
    }

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

    private fun DetailIntermediate.toDetailModel(): DetailModel {

        val tagMap = tagEntities.groupBy(TagEntity::type) {
            TagModel(it.type, it.name)
        }

        val thumbnails = remoteImageEntities.map {
            val width = it.thumbnailWidth.coerceAtLeast(1).toFloat()
            val height = it.thumbnailHeight.coerceAtLeast(1).toFloat()
            Thumbnail(
                aspectRatio = width / height,
                path = it.thumbnailPath
            )
        }

        return DetailModel(
            title = bookEntity.title,
            pageCount = bookEntity.pageCount,
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

    private fun GalleryDetailResponse.toEntity() =
        BookEntity(
            bookId = id,
            title = title.english,
            pageCount = num_pages,
            thumbnailWidth = thumbnail.width,
            thumbnailHeight = thumbnail.height,
            thumbnailPath = thumbnail.path
        )

    private fun GalleryDetailResponse.tagEntities(): List<TagEntity> = tags.map {
        TagEntity(
            bookId = id,
            name = it.name,
            type = it.type
        )
    }

    private fun GalleryDetailResponse.imageEntities(): List<ImageEntity> = pages.map { page ->
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
