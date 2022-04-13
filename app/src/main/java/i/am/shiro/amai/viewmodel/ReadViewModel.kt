package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.view.PageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers.io

class ReadViewModel(private val database: AmaiDatabase) : ViewModel() {

    private var disposable = Disposable.disposed()

    private var isLoaded = false

    val pagesLive = MutableLiveData<List<PageView>>()

    fun setBookId(bookId: Int) {
        if (isLoaded) return
        else isLoaded = true

        disposable = Single
            .fromCallable { database.pageDao.findByBookId(bookId) }
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(pagesLive::setValue)
    }

    override fun onCleared() {
        disposable.dispose()
    }
}