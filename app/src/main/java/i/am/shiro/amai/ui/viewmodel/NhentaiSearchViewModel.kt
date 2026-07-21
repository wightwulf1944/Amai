package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.data.repository.GalleryRepository
import i.am.shiro.amai.ui.viewmodel.utils.savedMutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class)
class NhentaiSearchViewModel(
    handle: SavedStateHandle,
    private val repository: GalleryRepository,
    private val query: String
) : ViewModel() {

    private val cacheId by handle.saved { Uuid.random() }

    val sortFlow by handle.savedMutableStateFlow(Sort.DATE)

    val books = sortFlow.flatMapLatest { sort ->
        repository.getSearchPager(
            cacheId = cacheId,
            query = query,
            sort = sort
        )
    }.cachedIn(viewModelScope)

    override fun onCleared() {
        viewModelScope.launch {
            repository.clearCache(cacheId)
        }
    }

    fun onSortChange(sort: Sort) {
        sortFlow.value = sort
    }
}
