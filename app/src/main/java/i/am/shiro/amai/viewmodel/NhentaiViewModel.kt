package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.entity.RemoteImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.network.BookJson
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.SearchJson
import i.am.shiro.amai.util.imageEntities
import i.am.shiro.amai.util.invoke
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import timber.log.Timber
import java.util.*

private const val PAGING_THRESHOLD = 10

// TODO reimplement Rx calls so that they chain and produce a single disposable
class NhentaiViewModel(
    handle: SavedStateHandle,
    private val database: AmaiDatabase,
    private val preferences: AmaiPreferences,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private var deleteDisposable = Disposable.disposed()

    private var localDisposable = Disposable.disposed()

    private var remoteDisposable = Disposable.disposed()

    private var query by handle<String>("")

    private var page by handle<Int>(0)

    private var sort by handle<Nhentai.Sort>(Nhentai.Sort.DATE)

    private var isComplete by handle<Boolean>(false)

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
        if (isComplete) return
        if (!remoteDisposable.isDisposed) return
        if (position > booksLive.value!!.size - PAGING_THRESHOLD) fetchRemotePage()
    }

    fun onRefresh() {
        page = 0
        isComplete = false

        deleteLocalThen {
            fetchRemotePage()
        }
    }

    fun onSort(sort: Nhentai.Sort) {
        page = 0
        isComplete = false
        this.sort = sort

        deleteLocalThen {
            fetchRemotePage()
        }
    }

    fun onSearch(query: String) {
        page = 0
        isComplete = false
        this.query = query

        deleteLocalThen {
            fetchRemotePage()
        }
    }

    private fun deleteLocalThen(onComplete: () -> Unit) {
        deleteDisposable.dispose()
        deleteDisposable = Completable
            .concatArray(
                database.cachedDao.deleteAll(),
                database.bookDao.deleteOrphan()
            )
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(onComplete)
    }

    private fun fetchLocal() {
        localDisposable.dispose()
        localDisposable = database.cachedPreviewDao
            .getAll()
            .subscribe(booksLive::postValue)
    }

    private fun fetchRemotePage() {
        remoteDisposable.dispose()

        if (query.matches(Regex("^id:\\d+\$"))) {
            val id = query.substringAfter("id:").toInt()

            remoteDisposable = nhentaiApi.getBook(id)
                .doOnSubscribe { isLoadingLive.postValue(true) }
                .doFinally { isLoadingLive.postValue(false) }
                .subscribe(::onGetBookSuccess, Timber::e)

        } else {
            val query = "${preferences.searchConstants} $query"
            val page = page + 1

            remoteDisposable = nhentaiApi.search(query, page, sort)
                .doOnSubscribe { isLoadingLive.postValue(true) }
                .doFinally { isLoadingLive.postValue(false) }
                .subscribe(::onSearchSuccess, Timber::e)
        }
    }

    private fun onGetBookSuccess(bookJson: BookJson) {
        val cachedEntities = listOf(CachedEntity(0, bookJson.id))
        val bookEntities = listOf(bookJson.toEntity())
        val tagEntities = bookJson.tagEntities()
        val imageEntities = bookJson.imageEntities()

        with(database) {
            runInTransaction {
                cachedDao.insert(cachedEntities)
                bookDao.insert(bookEntities)
                tagDao.insert(tagEntities)
                remoteImageDao.insert(imageEntities)
            }
        }

        isComplete = true
    }

    private fun onSearchSuccess(searchJson: SearchJson) {
        val cachedEntities = LinkedList<CachedEntity>()
        val bookEntities = LinkedList<BookEntity>()
        val tagEntities = LinkedList<TagEntity>()
        val imageEntities = LinkedList<RemoteImageEntity>()

        for (book in searchJson.result) {
            cachedEntities += CachedEntity(0, book.id)
            bookEntities += book.toEntity()
            tagEntities += book.tagEntities()
            imageEntities += book.imageEntities()
        }

        with(database) {
            runInTransaction {
                cachedDao.insert(cachedEntities)
                bookDao.insert(bookEntities)
                tagDao.insert(tagEntities)
                remoteImageDao.insert(imageEntities)
            }
        }

        if (page < searchJson.num_pages) {
            ++page
        } else {
            isComplete = true
        }
    }
}

