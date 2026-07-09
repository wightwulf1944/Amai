package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import i.am.shiro.amai.data.repository.FavoritesRepository
import i.am.shiro.amai.model.FavoritesSort
import i.am.shiro.amai.ui.viewmodel.utils.savedMutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@OptIn(SavedStateHandleSaveableApi::class)
class FavoritesViewModel(
    handle: SavedStateHandle,
    repository: FavoritesRepository
) : ViewModel() {

    val query by handle.savedMutableStateFlow("")

    val sort by handle.savedMutableStateFlow(FavoritesSort.New)

    val books = repository.getFavorites(query, sort)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(query: String) {
        this.query.value = query
    }

    fun onSortChange(sort: FavoritesSort) {
        this.sort.value = sort
    }
}
