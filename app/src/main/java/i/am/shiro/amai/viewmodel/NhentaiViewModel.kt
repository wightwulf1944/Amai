package i.am.shiro.amai.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.PaginatedResponse
import i.am.shiro.amai.util.toEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class NhentaiViewModel(
    handle: SavedStateHandle,
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private var query by handle.saved { "" }

    private var page by handle.saved { 0 }

    private var sort by handle.saved { Nhentai.Sort.DATE }

    private var isComplete by handle.saved { false }

    val books = database.cachedPreviewDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var isLoading by mutableStateOf(false)
        private set

    init {
        if (page == 0) {
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

    fun sort(sort: Nhentai.Sort) {
        if (this.sort == sort) return
        this.sort = sort
        refresh()
    }

    fun search(query: String) {
        if (this.query == query) return
        this.query = query
        refresh()
    }

    // FIXME sort is ignored on empty query
    private fun fetchRemotePage() {
        viewModelScope.launch {
            isLoading = true
            try {
                // delete cache first before loading page 1
                if (page == 0) {
                    database.cachedDao.deleteAll()
                    database.bookDao.deleteOrphan()
                }

                if (query.isEmpty()) {
                    val response = nhentaiApi.getAll(page + 1)
                    onSearchSuccess(response)
                } else if (query.matches(Regex("""^id:\d+$"""))) {
                    val id = query.substringAfter("id:").toInt()
                    val response = nhentaiApi.getOne(id)
                    onGetBookSuccess(response)
                } else {
                    val response = nhentaiApi.search(query, sort, page + 1)
                    onSearchSuccess(response)
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun onGetBookSuccess(bookJson: GalleryDetailResponse) {
        database.withTransaction {
            database.cachedDao.insert(CachedEntity(0, bookJson.id))
            database.bookDao.insert(bookJson.toEntity())
        }
        isComplete = true
    }

    private suspend fun onSearchSuccess(searchJson: PaginatedResponse) {
        database.withTransaction {
            for (bookJson in searchJson.result) {
                database.cachedDao.insert(CachedEntity(0, bookJson.id))
                database.bookDao.insert(bookJson.toEntity())
            }
        }

        if (page < searchJson.num_pages) {
            page++
        } else {
            isComplete = true
        }
    }
}
