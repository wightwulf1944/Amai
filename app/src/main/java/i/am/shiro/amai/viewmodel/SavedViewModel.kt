package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.model.Book
import io.reactivex.disposables.Disposables
import io.realm.Case
import io.realm.Realm
import io.realm.kotlin.where

class SavedViewModel : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private var disposable = Disposables.disposed()

    val booksLive = MutableLiveData<List<Book>>()

    init {
        filterBooks("")
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
        realm.close()
    }

    fun filterBooks(s: String) {
        disposable.dispose()

        disposable = realm.where<Book>()
            .equalTo("isDownloaded", true)
            .contains("title", s, Case.INSENSITIVE)
            .findAllAsync()
            .asFlowable()
            .filter { it.isLoaded }
            .map { realm.copyFromRealm(it) }
            .subscribe { booksLive.postValue(it) }
    }

    fun onBookDelete(bookId: Int) {
        realm.executeTransaction {
            it.where<Book>()
                .equalTo("id", bookId)
                .findAll()
                .deleteAllFromRealm()
        }
    }
}
