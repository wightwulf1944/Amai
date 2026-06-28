package i.am.shiro.amai.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import i.am.shiro.amai.network.Nhentai.Sort
import i.am.shiro.amai.repository.GalleryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(SavedStateHandleSaveableApi::class)
class NhentaiViewModel(
    handle: SavedStateHandle,
    private val repository: GalleryRepository
) : ViewModel() {

    private var query by handle.saved { "" }

    private var page by handle.saved { 0 }

    private var sort by handle.saved { Sort.DATE }

    private var isComplete by handle.saved { false }

    var isLoading by handle.saveable { mutableStateOf(false) }
        private set

    val books = repository.getCachedBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        if (isLoading) {
            fetchRemotePage()
        }
    }

    fun loadMore() {
        if (isComplete || isLoading) return
        fetchRemotePage()
    }

    fun refresh() {
        page = 0
        isComplete = false
        fetchRemotePage()
    }

    fun sort(sort: Sort) {
        if (this.sort == sort) return
        this.sort = sort
        refresh()
    }

    fun search(query: String) {
        if (this.query == query) return
        this.query = query
        refresh()
    }

    private fun fetchRemotePage() {
        isLoading = true
        viewModelScope.launch {
            try {
                val totalPages = repository.searchGalleryPage(query, sort, page + 1)

                if (page < totalPages) {
                    page++
                } else {
                    isComplete = true
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                isLoading = false
            }
        }
    }
}
