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
import kotlinx.coroutines.flow.map

class GalleryRepository(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) {
    fun getCachedBooks() = database.intermediateDao.getCachedPreviews()
        .map { list -> list.map { it.toModel() } }

    suspend fun getLatestGalleryPage(page: Int) =
        fetchPage(page) { getAll(page) }

    suspend fun searchGalleryPage(query: String, sort: Sort, page: Int) =
        fetchPage(page) { search(query, sort, page) }

    suspend fun getTaggedGalleryPage(tagId: Int, sort: Sort, page: Int) =
        fetchPage(page) { getTagged(tagId, sort, page) }

    private suspend fun fetchPage(page: Int, call: suspend Nhentai.Api.() -> PaginatedDto): Int {
        if (page == 1) {
            database.withTransaction {
                database.cachedDao.deleteAll()
                database.bookDao.deleteOrphan()
            }
        }

        val response = nhentaiApi.call()

        database.withTransaction {
            for (bookJson in response.result) {
                database.cachedDao.insert(CachedEntity(0, bookJson.id))
                database.bookDao.insert(bookJson.toEntity())
            }
        }

        return response.num_pages
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
