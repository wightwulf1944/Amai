package i.am.shiro.amai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.DATABASE
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.dto.BookJson
import i.am.shiro.amai.util.imageEntities
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import io.reactivex.disposables.Disposables
import timber.log.Timber

class LoadingViewModel : ViewModel() {

    private var disposable = Disposables.disposed()

    private val isLoadedLive = MutableLiveData<Boolean>()

    override fun onCleared() {
        disposable.dispose()
    }

    fun load(bookId: Int): LiveData<Boolean> {
        if (disposable.isDisposed) {
            disposable = Nhentai.API
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

        DATABASE.runInTransaction {
            DATABASE.bookDao.insert(bookEntity)
            DATABASE.tagDao.insert(tagEntities)
            DATABASE.remoteImageDao.insert(imageEntities)
        }

        isLoadedLive.postValue(true)
    }
}