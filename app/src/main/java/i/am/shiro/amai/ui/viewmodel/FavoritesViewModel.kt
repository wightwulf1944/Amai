package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import i.am.shiro.amai.data.repository.FavoritesRepository
import i.am.shiro.amai.model.FavoritesSort
import i.am.shiro.amai.ui.viewmodel.utils.savedMutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    handle: SavedStateHandle,
    repository: FavoritesRepository
) : ViewModel() {

    val query by handle.savedMutableStateFlow("")

    val sort by handle.savedMutableStateFlow(FavoritesSort.New)

    val books = query.debounce(100.milliseconds)
        .combine(sort) { q, s -> q to s }
        .flatMapLatest { (q, s) -> repository.getFavorites(q, s) }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        this.query.value = query
    }

    fun onSortChange(sort: FavoritesSort) {
        this.sort.value = sort
    }
}
