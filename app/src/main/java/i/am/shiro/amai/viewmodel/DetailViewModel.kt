package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.model.DetailModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers.io

class DetailViewModel(private val database: AmaiDatabase) : ViewModel() {

    private var disposable = Disposables.disposed()

    private var isLoaded = false

    val modelLive = MutableLiveData<DetailModel>()

    fun setBookId(bookId: Int) {
        if (isLoaded) return
        else isLoaded = true

        disposable = Single
            .fromCallable { getModel(bookId) }
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(modelLive::setValue)
    }

    private fun getModel(bookId: Int) = DetailModel(
        book = database.bookDao.findById(bookId),
        artistTags = database.tagDao.findNameByIdAndType(bookId, "artist"),
        groupTags = database.tagDao.findNameByIdAndType(bookId, "group"),
        parodyTags = database.tagDao.findNameByIdAndType(bookId, "parody"),
        characterTags = database.tagDao.findNameByIdAndType(bookId, "character"),
        languageTags = database.tagDao.findNameByIdAndType(bookId, "language"),
        categoryTags = database.tagDao.findNameByIdAndType(bookId, "category"),
        generalTags = database.tagDao.findNameByIdAndType(bookId, "tag"),
        pageImages = database.thumbnailDao.findByBookId(bookId)
    )

    override fun onCleared() {
        disposable.dispose()
    }
}