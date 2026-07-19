package i.am.shiro.amai.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.withTransaction
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.intermediate.CachedPreviewIntermediate
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.remote.paging.NhentaiRemoteMediator
import i.am.shiro.amai.model.BookPreview
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class PagingGalleryRepository(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getLatestPager(cacheId: Uuid) = Pager(
        config = PagingConfig(
            pageSize = 50,
            initialLoadSize = 50,
            prefetchDistance = 25
        ),
        remoteMediator = NhentaiRemoteMediator(cacheId, nhentaiApi, database),
        pagingSourceFactory = { database.intermediateDao.getCachedPreviewsPaging(cacheId) }
    ).flow.map { pagingData ->
        pagingData.map { it.toModel() }
    }

    private fun CachedPreviewIntermediate.toModel() = BookPreview(
        bookId = book.bookId,
        aspectRatio = book.thumbnailWidth.toFloat() / book.thumbnailHeight.toFloat(),
        thumbnailPath = book.thumbnailPath,
        title = book.title,
        showFavoriteBadge = favorite != null,
        pageCount = book.pageCount
    )

    suspend fun clearCache(cacheId: Uuid) {
        database.withTransaction {
            database.galleryCacheDao.deleteById(cacheId)
            database.bookDao.deleteOrphan()
        }
    }
}
