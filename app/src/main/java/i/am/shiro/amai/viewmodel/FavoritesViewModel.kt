package i.am.shiro.amai.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.intermediate.FavoritesPreviewIntermediate
import i.am.shiro.amai.model.BookPreview
import i.am.shiro.amai.util.savedMutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    handle: SavedStateHandle,
    database: AmaiDatabase
) : ViewModel() {

    private val query by handle.savedMutableStateFlow("")
    private val sort by handle.savedMutableStateFlow(FavoritesSort.New)

    val favoriteBooks = database.intermediateDao.getAllFavorites()
        .combine(query) { list, q ->
            // TODO improve search by matching more than just title
            if (q.isBlank()) list else list.filter { it.book.title.contains(q, ignoreCase = true) }
        }
        .combine(sort) { list, s ->
            when (s) {
                FavoritesSort.New -> list.sortedByDescending { it.favorite.favoriteDate }
                FavoritesSort.Old -> list.sortedBy { it.favorite.favoriteDate }
            }
        }
        .map { list -> list.map { it.toView() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearch(query: String) {
        this.query.value = query
    }

    fun onSort(sort: FavoritesSort) {
        this.sort.value = sort
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
