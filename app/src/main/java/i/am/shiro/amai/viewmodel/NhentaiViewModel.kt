package i.am.shiro.amai.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.PaginatedResponse
import i.am.shiro.amai.util.invoke
import i.am.shiro.amai.util.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class NhentaiViewModel(
    handle: SavedStateHandle,
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private var query by handle<String>("")

    private var page by handle<Int>(0)

    private var sort by handle<Nhentai.Sort>(Nhentai.Sort.DATE)

    private var isComplete by handle<Boolean>(false)

    val books: StateFlow<List<CachedPreviewView>> = database.cachedPreviewDao
        .getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        if (page == 0) {
            onRefresh()
        }
    }

    fun onScrollToBottom() {
        if (isComplete || _isLoading.value) return
        fetchRemotePage()
    }

    fun onRefresh() {
        page = 0
        isComplete = false

        viewModelScope.launch {
            deleteLocal()
            fetchRemotePage()
        }
    }

    fun onSort(sort: Nhentai.Sort) {
        if (this.sort == sort) return
        this.sort = sort
        onRefresh()
    }

    fun onSearch(query: String) {
        if (this.query == query) return
        this.query = query
        onRefresh()
    }

    private suspend fun deleteLocal() {
        try {
            database.cachedDao.deleteAll()
            database.bookDao.deleteOrphan()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun fetchRemotePage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
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
                _isLoading.value = false
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
