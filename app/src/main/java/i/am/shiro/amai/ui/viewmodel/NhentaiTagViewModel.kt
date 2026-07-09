package i.am.shiro.amai.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.data.repository.GalleryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(SavedStateHandleSaveableApi::class)
class NhentaiTagViewModel(
    handle: SavedStateHandle,
    private val tagId: Int,
    private val repository: GalleryRepository
) : ViewModel() {

    private var page by handle.saved { 0 }

    private var isComplete by handle.saved { false }

    var sort by handle.saveable { mutableStateOf(Sort.DATE) }
        private set

    var isLoading by handle.saveable { mutableStateOf(true) }
        private set

    val books = repository.getCachedBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        if (isLoading) fetchRemotePage()
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

    private fun fetchRemotePage() {
        isLoading = true
        viewModelScope.launch {
            try {
                val totalPages = repository.getTaggedGalleryPage(tagId, sort, page + 1)

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
