package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import i.am.shiro.amai.data.remote.Nhentai
import i.am.shiro.amai.data.repository.GalleryRepository
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class NhentaiSearchViewModel(
    handle: SavedStateHandle,
    private val repository: GalleryRepository,
    private val query: String,
    private val sort: Nhentai.Sort,
) : ViewModel() {

    private val cacheId by handle.saved { Uuid.random() }

    val books = repository.getSearchPager(
        cacheId = cacheId,
        query = query,
        sort = sort
    ).cachedIn(viewModelScope)

    override fun onCleared() {
        viewModelScope.launch {
            repository.clearCache(cacheId)
        }
    }
}
