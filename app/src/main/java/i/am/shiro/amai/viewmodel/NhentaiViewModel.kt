package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.DATABASE
import i.am.shiro.amai.NhentaiSort
import i.am.shiro.amai.Preferences
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.entity.RemoteImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.dto.SearchResponseJson
import i.am.shiro.amai.util.delegate
import i.am.shiro.amai.util.imageEntities
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers.io
import timber.log.Timber
import java.util.*

private const val PAGING_THRESHOLD = 10

class NhentaiViewModel(handle: SavedStateHandle) : ViewModel() {

    private var deleteDisposable = Disposables.disposed()

    private var localDisposable = Disposables.disposed()

    private var remoteDisposable = Disposables.disposed()

    private var query by handle.delegate("")

    private var page by handle.delegate(0)

    private var sort by handle.delegate(NhentaiSort.New)

    val booksLive = MutableLiveData<List<CachedPreviewView>>()

    val isLoadingLive = MutableLiveData<Boolean>()

    init {
        if (page == 0) {
            deleteLocalThen {
                fetchLocal()
                fetchRemotePage()
            }
        } else {
            fetchLocal()
        }
    }

    override fun onCleared() {
        deleteDisposable.dispose()
        localDisposable.dispose()
        remoteDisposable.dispose()
    }

    fun onPositionBind(position: Int) {
        if (!remoteDisposable.isDisposed) return
        if (position > booksLive.value!!.size - PAGING_THRESHOLD) fetchRemotePage()
    }

    fun onSortOrderChanged(sort: NhentaiSort) {
        page = 0
        this.sort = sort

        deleteLocalThen {
            fetchLocal()
            fetchRemotePage()
        }
    }

    fun search(query: String) {
        page = 0
        this.query = query

        deleteLocalThen {
            fetchRemotePage()
        }
    }

    private fun deleteLocalThen(onComplete: () -> Unit) {
        deleteDisposable.dispose()
        deleteDisposable = Completable
            .concatArray(
                DATABASE.cachedDao.deleteAll(),
                DATABASE.bookDao.deleteOrphan()
            )
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(onComplete)
    }

    private fun fetchLocal() {
        localDisposable.dispose()
        localDisposable = DATABASE.cachedPreviewDao
            .getAllSorted(sort)
            .doOnNext { Timber.d("$it") }
            .subscribe(booksLive::postValue)
    }

    private fun fetchRemotePage() {
        val query = "${Preferences.getSearchConstants()} $query"
        val page = page + 1
        val sort = sort.toString()

        remoteDisposable.dispose()
        remoteDisposable = Nhentai.API.search(query, page, sort)
            .doOnSubscribe { isLoadingLive.postValue(true) }
            .doFinally { isLoadingLive.postValue(false) }
            .subscribe(::onRemoteSuccess, Timber::e)
    }

    private fun onRemoteSuccess(searchResponseJson: SearchResponseJson) {
        val cachedEntities = LinkedList<CachedEntity>()
        val bookEntities = LinkedList<BookEntity>()
        val tagEntities = LinkedList<TagEntity>()
        val imageEntities = LinkedList<RemoteImageEntity>()

        for (book in searchResponseJson.results) {
            cachedEntities += CachedEntity(book.id)
            bookEntities += book.toEntity()
            tagEntities += book.tagEntities()
            imageEntities += book.imageEntities()
        }

        DATABASE.runInTransaction {
            DATABASE.cachedDao.insert(cachedEntities)
            DATABASE.bookDao.insert(bookEntities)
            DATABASE.tagDao.insert(tagEntities)
            DATABASE.remoteImageDao.insert(imageEntities)
        }

        if (page < searchResponseJson.pageTotal) ++page
    }
}

