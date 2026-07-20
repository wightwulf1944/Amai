package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import i.am.shiro.amai.data.remote.Nhentai.Sort
import i.am.shiro.amai.data.repository.PagingGalleryRepository
import i.am.shiro.amai.ui.viewmodel.utils.savedMutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class)
class NhentaiTagViewModel(
    handle: SavedStateHandle,
    private val repository: PagingGalleryRepository,
    private val tagId: Int
) : ViewModel() {

    private val cacheId by handle.saved { Uuid.random() }

    val sortFlow by handle.savedMutableStateFlow(Sort.DATE)

    val books = sortFlow.flatMapLatest { sort ->
        repository.getTaggedPager(
            cacheId = cacheId,
            tagId = tagId,
            sort = sort
        )
    }.cachedIn(viewModelScope)

    override fun onCleared() {
        viewModelScope.launch {
            repository.clearCache(cacheId)
        }
    }

    fun onSortChange(newSort: Sort) {
        sortFlow.value = newSort
    }
}
