package i.am.shiro.amai.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import i.am.shiro.amai.data.repository.GalleryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(SavedStateHandleSaveableApi::class)
class HomepageViewModel(
    handle: SavedStateHandle,
    private val repository: GalleryRepository
) : ViewModel() {

    private var page by handle.saved { 0 }

    private var isComplete by handle.saved { false }

    var isLoading by handle.saveable { mutableStateOf(true) }
        private set

    val books = repository.getCachedBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        if (isLoading) fetchRemotePage()
    }

    fun loadMore() {
        if (isLoading || isComplete) return
        fetchRemotePage()
    }

    fun refresh() {
        page = 0
        isComplete = false
        fetchRemotePage()
    }

    private fun fetchRemotePage() {
        isLoading = true
        viewModelScope.launch {
            try {
                val totalPages = repository.fetchGalleryPage(page + 1)
                
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
