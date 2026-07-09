package i.am.shiro.amai.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.data.repository.GalleryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class NhentaiLatestViewModel(
    handle: SavedStateHandle,
    private val repository: GalleryRepository
) : ViewModel() {

    private var page by handle.saved { 0 }

    private var isComplete by handle.saved { false }

    private var fetchJob: Job? = null

    var isLoading by mutableStateOf(false)
        private set

    val books = repository.getCachedBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        if (page == 0) fetchRemotePage()
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

    private fun fetchRemotePage() {
        val requestedPage = page + 1

        isLoading = true

        fetchJob = viewModelScope.launch {
            try {
                val isLastPage = repository.getLatestGalleryPage(requestedPage)

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
