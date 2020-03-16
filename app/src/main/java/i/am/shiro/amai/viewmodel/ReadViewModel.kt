package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.view.PageView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers.io

class ReadViewModel(private val database: AmaiDatabase) : ViewModel() {

    private var disposable = Disposables.disposed()

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