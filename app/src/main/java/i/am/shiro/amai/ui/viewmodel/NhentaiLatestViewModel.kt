package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import i.am.shiro.amai.data.repository.GalleryRepository
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class NhentaiLatestViewModel(
    handle: SavedStateHandle,
    private val repository: GalleryRepository
) : ViewModel() {

    private val cacheId by handle.saved { Uuid.random() }

    val books = repository.getLatestPager(cacheId)
        .cachedIn(viewModelScope)

    override fun onCleared() {
        viewModelScope.launch {
            repository.clearCache(cacheId)
        }
    }
}
