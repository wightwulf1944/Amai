package i.am.shiro.amai.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.util.savedMutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    handle: SavedStateHandle,
    database: AmaiDatabase
) : ViewModel() {

    private val query by handle.savedMutableStateFlow("")
    private val sort by handle.savedMutableStateFlow(FavoritesSort.New)

    val favoriteBooks = combine(query, sort, ::Pair)
        .flatMapLatest { (q, s) -> database.favoritesPreviewDao.find(q, s) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearch(query: String) {
        this.query.value = query
    }

    fun onSort(sort: FavoritesSort) {
        this.sort.value = sort
    }
}
