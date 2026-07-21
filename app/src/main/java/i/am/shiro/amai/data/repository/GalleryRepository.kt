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

@OptIn(ExperimentalPagingApi::class)
class GalleryRepository(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) {
    private val pagingConfig = PagingConfig(
        pageSize = 50,
        initialLoadSize = 50,
        prefetchDistance = 25
    )

    fun getLatestPager(cacheId: Uuid) = Pager(
        config = pagingConfig,
        pagingSourceFactory = { database.intermediateDao.getCachedPreviewsPaging(cacheId) },
        remoteMediator = NhentaiRemoteMediator(cacheId, database) { page, pageSize ->
            nhentaiApi.getAll(page = page, perPage = pageSize)
        }
    ).flow.map { pagingData ->
        pagingData.map { it.toModel() }
    }

    fun getTaggedPager(cacheId: Uuid, tagId: Int, sort: Nhentai.Sort) = Pager(
        config = pagingConfig,
        pagingSourceFactory = { database.intermediateDao.getCachedPreviewsPaging(cacheId) },
        remoteMediator = NhentaiRemoteMediator(cacheId, database) { page, pageSize ->
            nhentaiApi.getTagged(tagId = tagId, sort = sort, page = page, perPage = pageSize)
        }
    ).flow.map { pagingData ->
        pagingData.map { it.toModel() }
    }

    fun getSearchPager(cacheId: Uuid, query: String, sort: Nhentai.Sort) = Pager(
        config = pagingConfig,
        pagingSourceFactory = { database.intermediateDao.getCachedPreviewsPaging(cacheId) },
        remoteMediator = NhentaiRemoteMediator(cacheId, database) { page, _ ->
            nhentaiApi.search(query = query, sort = sort, page = page)
        }
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

    suspend fun clearAllCache() = database.withTransaction {
        database.galleryCacheDao.clearAll()
        database.bookDao.deleteOrphan()
    }
}
