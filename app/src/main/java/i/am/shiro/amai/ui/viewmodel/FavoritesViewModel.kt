package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.data.repository.FavoritesRepository
import i.am.shiro.amai.model.FavoritesSort
import i.am.shiro.amai.ui.viewmodel.utils.savedMutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    handle: SavedStateHandle,
    repository: FavoritesRepository
) : ViewModel() {

    private val query by handle.savedMutableStateFlow("")
    private val sort by handle.savedMutableStateFlow(FavoritesSort.New)

    val favoriteBooks = repository.getFavorites(query, sort)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearch(query: String) {
        this.query.value = query
    }

    fun onSort(sort: FavoritesSort) {
        this.sort.value = sort
    }
}
