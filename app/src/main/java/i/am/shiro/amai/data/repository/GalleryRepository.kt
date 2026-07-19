package i.am.shiro.amai.data.repository

import androidx.room.withTransaction
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.GalleryCacheEntity
import i.am.shiro.amai.data.local.entity.GalleryCacheEntryEntity
import i.am.shiro.amai.data.local.intermediate.CachedPreviewIntermediate
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.data.remote.dto.GalleryListItemDto
import i.am.shiro.amai.data.remote.dto.PaginatedDto
import i.am.shiro.amai.model.BookPreview
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.map

class GalleryRepository(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) {
    fun getCachedBooks(cacheId: Uuid) = database.intermediateDao.getCachedPreviews(cacheId)
        .map { list -> list.map { it.toModel() } }

    suspend fun clearCache(cacheId: Uuid) = database.withTransaction {
        database.galleryCacheDao.deleteById(cacheId)
        database.bookDao.deleteOrphan()
    }

    suspend fun clearAllCache() = database.withTransaction {
        database.galleryCacheDao.clearAll()
        database.bookDao.deleteOrphan()
    }

    suspend fun fetchLatestPage(cacheId: Uuid, page: Int) =
        fetchPage(cacheId, page) { getAll(page) }

    suspend fun fetchTaggedPage(cacheId: Uuid, tagId: Int, sort: Sort, page: Int) =
        fetchPage(cacheId, page) { getTagged(tagId, sort, page) }

    suspend fun fetchSearchPage(cacheId: Uuid, query: String, sort: Sort, page: Int) =
        fetchPage(cacheId, page) { search(query, sort, page) }

    private suspend fun fetchPage(
        cacheId: Uuid,
        page: Int,
        call: suspend Nhentai.Api.() -> PaginatedDto
    ): Boolean {
        if (page == 1) {
            database.withTransaction {
                database.galleryCacheDao.deleteById(cacheId)
                database.bookDao.deleteOrphan()
            }
        }

        val response = nhentaiApi.call()

        database.withTransaction {
            database.galleryCacheDao.upsert(GalleryCacheEntity(cacheId, null))
            for (bookJson in response.result) {
                database.galleryCacheEntryDao.insert(GalleryCacheEntryEntity(0, cacheId, bookJson.id))
                database.bookDao.insert(bookJson.toEntity())
            }
        }

        return page >= response.num_pages // returns true on last page
    }

    private fun CachedPreviewIntermediate.toModel() = BookPreview(
        bookId = book.bookId,
        aspectRatio = book.thumbnailWidth.toFloat() / book.thumbnailHeight.toFloat(),
        thumbnailPath = book.thumbnailPath,
        title = book.title,
        showFavoriteBadge = favorite != null,
        pageCount = book.pageCount
    )

    private fun GalleryListItemDto.toEntity() = BookEntity(
        bookId = id,
        title = english_title,
        pageCount = num_pages,
        thumbnailWidth = thumbnail_width,
        thumbnailHeight = thumbnail_height,
        thumbnailPath = thumbnail
    )
}
