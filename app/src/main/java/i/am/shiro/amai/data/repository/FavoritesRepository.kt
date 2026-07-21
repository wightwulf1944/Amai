package i.am.shiro.amai.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.intermediate.FavoritesPreviewIntermediate
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.model.FavoritesSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    private val database: AmaiDatabase
) {
    fun getFavorites(
        query: String,
        sort: FavoritesSort
    ): Flow<PagingData<BookPreview>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            initialLoadSize = 50,
            prefetchDistance = 25
        ),
        pagingSourceFactory = {
            database.intermediateDao.getFavoritesPaging(
                query = query,
                isDescending = sort == FavoritesSort.New
            )
        }
    ).flow.map { pagingData ->
        pagingData.map { it.toView() }
    }

    private fun FavoritesPreviewIntermediate.toView() = BookPreview(
        bookId = favorite.bookId,
        aspectRatio = book.thumbnailWidth.toFloat() / book.thumbnailHeight.toFloat(),
        thumbnailPath = book.thumbnailPath,
        title = book.title,
        showFavoriteBadge = false,
        pageCount = book.pageCount,
    )
}
