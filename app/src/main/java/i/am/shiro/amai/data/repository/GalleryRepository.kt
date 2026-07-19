package i.am.shiro.amai.data.repository

import androidx.room.withTransaction
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.CachedEntity
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
    fun getCachedBooks(cacheKey: Uuid) = database.intermediateDao.getCachedPreviews(cacheKey)
        .map { list -> list.map { it.toModel() } }

    suspend fun clearCache(cacheKey: Uuid) = database.withTransaction {
        database.cachedDao.clearCache(cacheKey)
        database.bookDao.deleteOrphan()
    }

    suspend fun clearAllCache() = database.withTransaction {
        database.cachedDao.clearCache()
        database.bookDao.deleteOrphan()
    }

    suspend fun fetchLatestPage(cacheKey: Uuid, page: Int) =
        fetchPage(cacheKey, page) { getAll(page) }

    suspend fun fetchTaggedPage(cacheKey: Uuid, tagId: Int, sort: Sort, page: Int) =
        fetchPage(cacheKey, page) { getTagged(tagId, sort, page) }

    suspend fun fetchSearchPage(cacheKey: Uuid, query: String, sort: Sort, page: Int) =
        fetchPage(cacheKey, page) { search(query, sort, page) }

    private suspend fun fetchPage(
        cacheKey: Uuid,
        page: Int,
        call: suspend Nhentai.Api.() -> PaginatedDto
    ): Boolean {
        if (page == 1) {
            database.withTransaction {
                database.cachedDao.clearCache(cacheKey)
                database.bookDao.deleteOrphan()
            }
        }

        val response = nhentaiApi.call()

        database.withTransaction {
            for (bookJson in response.result) {
                database.cachedDao.insert(CachedEntity(0, cacheKey, bookJson.id))
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
