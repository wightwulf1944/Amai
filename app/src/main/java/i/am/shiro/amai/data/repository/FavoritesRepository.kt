package i.am.shiro.amai.data.repository

import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.intermediate.FavoritesPreviewIntermediate
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.model.FavoritesSort
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class FavoritesRepository(
    private val database: AmaiDatabase
) {
    fun getFavorites(
        queryFlow: Flow<String>,
        sortFlow: Flow<FavoritesSort>
    ) = database.intermediateDao.getAllFavorites()
        .combine(queryFlow.debounce(100.milliseconds)) { list, q ->
            list.filter { it.book.title.contains(q, ignoreCase = true) }
        }
        .combine(sortFlow) { list, s ->
            when (s) {
                FavoritesSort.New -> list.sortedByDescending { it.favorite.favoriteDate }
                FavoritesSort.Old -> list.sortedBy { it.favorite.favoriteDate }
            }
        }
        .map { list -> list.map { it.toView() } }

    private fun FavoritesPreviewIntermediate.toView() = BookPreview(
        bookId = favorite.bookId,
        aspectRatio = book.thumbnailWidth.toFloat() / book.thumbnailHeight.toFloat(),
        thumbnailPath = book.thumbnailPath,
        title = book.title,
        showFavoriteBadge = false,
        pageCount = book.pageCount,
    )
}
