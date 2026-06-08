package i.am.shiro.amai.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.view.FavoritesPreviewView
import i.am.shiro.amai.util.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    handle: SavedStateHandle,
    private val database: AmaiDatabase
) : ViewModel() {

    private var query by handle<String>("")

    private var sort by handle<FavoritesSort>(FavoritesSort.New)

    val books: StateFlow<List<FavoritesPreviewView>> = combine(
        handle.getStateFlow("query", ""),
        handle.getStateFlow("sort", FavoritesSort.New)
    ) { q, s -> q to s }
        .flatMapLatest { (q, s) ->
            database.favoritesPreviewDao.find(q, s)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearch(query: String) {
        this.query = query
    }

    fun onSort(sort: FavoritesSort) {
        this.sort = sort
    }
}
