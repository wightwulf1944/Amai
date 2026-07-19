package i.am.shiro.amai.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.GalleryCacheEntity
import i.am.shiro.amai.data.local.entity.GalleryCacheEntryEntity
import i.am.shiro.amai.data.local.intermediate.CachedPreviewIntermediate
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.remote.dto.GalleryListItemDto
import retrofit2.HttpException
import java.io.IOException
import kotlin.uuid.Uuid

@OptIn(ExperimentalPagingApi::class)
class NhentaiRemoteMediator(
    private val cacheId: Uuid,
    private val nhentaiApi: Nhentai.Api,
    private val database: AmaiDatabase
) : RemoteMediator<Int, CachedPreviewIntermediate>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedPreviewIntermediate>
    ) = when (loadType) {
        LoadType.PREPEND -> {
            // prepend is not supported
            MediatorResult.Success(endOfPaginationReached = true)
        }

        LoadType.REFRESH -> {
            fetchRemotePage(loadType, 1, state.config.pageSize)
        }

        LoadType.APPEND -> {
            val nextPage = database.galleryCacheDao.cacheById(cacheId)?.nextPage

            if (nextPage == null) {
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                fetchRemotePage(loadType, nextPage, state.config.pageSize)
            }
        }
    }

    private suspend fun fetchRemotePage(loadType: LoadType, page: Int, pageSize: Int) = try {
        val response = nhentaiApi.getAll(page = page, perPage = pageSize)
        val endReached = page >= response.num_pages

        database.withTransaction {
            if (loadType == LoadType.REFRESH) {
                database.galleryCacheDao.deleteById(cacheId)
                database.bookDao.deleteOrphan()
            }

            database.galleryCacheDao.upsert(
                GalleryCacheEntity(
                    id = cacheId,
                    nextPage = if (endReached) null else page + 1
                )
            )

            for (bookDto in response.result) {
                database.galleryCacheEntryDao.insert(
                    GalleryCacheEntryEntity(id = 0, cacheId = cacheId, bookId = bookDto.id)
                )
                database.bookDao.insert(bookDto.toEntity())
            }
        }

        MediatorResult.Success(endOfPaginationReached = endReached)
    } catch (e: IOException) {
        MediatorResult.Error(e)
    } catch (e: HttpException) {
        MediatorResult.Error(e)
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
