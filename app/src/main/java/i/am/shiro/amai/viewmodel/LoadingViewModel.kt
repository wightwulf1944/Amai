package i.am.shiro.amai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.network.BookJson
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.util.imageEntities
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

class LoadingViewModel(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private var disposable = Disposable.disposed()

    private val isLoadedLive = MutableLiveData<Boolean>()

    override fun onCleared() {
        disposable.dispose()
    }

    fun load(bookId: Int): LiveData<Boolean> {
        if (disposable.isDisposed) {
            disposable = nhentaiApi
                .getBook(bookId)
                .retry()
                .subscribe(::onFetch, Timber::e)
        }
        return isLoadedLive
    }

    private fun onFetch(bookJson: BookJson) {
        val bookEntity = bookJson.toEntity()
        val tagEntities = bookJson.tagEntities()
        val imageEntities = bookJson.imageEntities()

        database.runInTransaction {
            database.bookDao.insert(bookEntity)
            database.tagDao.insert(tagEntities)
            database.remoteImageDao.insert(imageEntities)
        }

        isLoadedLive.postValue(true)
    }
}