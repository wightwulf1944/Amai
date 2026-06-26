package i.am.shiro.amai.repository

import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.intermediate.FavoritesPreviewIntermediate
import i.am.shiro.amai.model.BookPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    private val database: AmaiDatabase
) {
    fun getFavorites(
        queryFlow: Flow<String>,
        sortFlow: Flow<FavoritesSort>
    ): Flow<List<BookPreview>> {
        return database.intermediateDao.getAllFavorites()
            .combine(queryFlow) { list, q ->
                // TODO improve search by matching more than just title
                if (q.isBlank()) list else list.filter { it.book.title.contains(q, ignoreCase = true) }
            }
            .combine(sortFlow) { list, s ->
                when (s) {
                    FavoritesSort.New -> list.sortedByDescending { it.favorite.favoriteDate }
                    FavoritesSort.Old -> list.sortedBy { it.favorite.favoriteDate }
                }
            }
            .map { list -> list.map { it.toView() } }
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
