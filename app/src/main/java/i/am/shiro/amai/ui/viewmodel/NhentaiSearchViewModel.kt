package i.am.shiro.amai.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.data.repository.GalleryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.Uuid

@OptIn(SavedStateHandleSaveableApi::class)
class NhentaiSearchViewModel(
    handle: SavedStateHandle,
    private val query: String,
    private val repository: GalleryRepository
) : ViewModel() {

    private val cacheId by handle.saved { Uuid.random() }

    private var page by handle.saved { 0 }

    private var isComplete by handle.saved { false }

    private var fetchJob: Job? = null

    var sort by handle.saveable { mutableStateOf(Sort.DATE) }
        private set

    var isLoading by mutableStateOf(false)
        private set

    val books = repository.getCachedBooks(cacheId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        if (page == 0) fetchRemotePage()
    }

    override fun onCleared() {
        viewModelScope.launch {
            repository.clearCache(cacheId)
        }
    }

    fun loadMore() {
        if (isComplete || fetchJob?.isActive == true) return
        fetchRemotePage()
    }

    fun refresh() {
        page = 0
        isComplete = false
        fetchJob?.cancel()
        fetchRemotePage()
    }

    fun onSortChange(sort: Sort) {
        if (this.sort == sort) return
        this.sort = sort
        refresh()
    }

    private fun fetchRemotePage() {
        val requestedPage = page + 1
        val requestedSort = sort

        isLoading = true

        fetchJob = viewModelScope.launch {
            try {
                val isLastPage = repository.fetchSearchPage(
                    cacheId = cacheId,
                    query = query,
                    sort = requestedSort,
                    page = requestedPage
                )

                if (isActive) {
                    page = requestedPage
                    isComplete = isLastPage
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                if (isActive) {
                    isLoading = false
                    fetchJob = null
                }
            }
        }
    }
}
