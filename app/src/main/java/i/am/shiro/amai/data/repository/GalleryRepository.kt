package i.am.shiro.amai.data.repository

import androidx.room.withTransaction
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.CachedEntity
import i.am.shiro.amai.data.local.intermediate.CachedPreviewIntermediate
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.data.remote.dto.GalleryListItemDto
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.data.remote.dto.PaginatedDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GalleryRepository(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) {
    fun getCachedBooks(): Flow<List<BookPreview>> {
        return database.intermediateDao.getCachedPreviews()
            .map { it.toModel() }
    }

    suspend fun fetchGalleryPage(page: Int): Int {
        val response = nhentaiApi.getAll(page)
        saveToCache(page, response)
        return response.num_pages
    }

    suspend fun searchGalleryPage(query: String, sort: Sort, page: Int): Int {
        val response = nhentaiApi.search(query, sort, page)
        saveToCache(page, response)
        return response.num_pages
    }

    private suspend fun saveToCache(page: Int, response: PaginatedDto) {
        database.withTransaction {
            if (page == 1) {
                database.cachedDao.deleteAll()
                database.bookDao.deleteOrphan()
            }

            for (bookJson in response.result) {
                database.cachedDao.insert(CachedEntity(0, bookJson.id))
                database.bookDao.insert(bookJson.toEntity())
            }
        }
    }

    private fun List<CachedPreviewIntermediate>.toModel() = map {
        BookPreview(
            bookId = it.book.bookId,
            aspectRatio = it.book.thumbnailWidth.toFloat() / it.book.thumbnailHeight.toFloat(),
            thumbnailPath = it.book.thumbnailPath,
            title = it.book.title,
            showFavoriteBadge = it.favorite != null,
            pageCount = it.book.pageCount
        )
    }

    private fun GalleryListItemDto.toEntity() = BookEntity(
        bookId = id,
        title = english_title,
        pageCount = num_pages,
        thumbnailWidth = thumbnail_width,
        thumbnailHeight = thumbnail_height,
        thumbnailPath = thumbnail
    )
}
