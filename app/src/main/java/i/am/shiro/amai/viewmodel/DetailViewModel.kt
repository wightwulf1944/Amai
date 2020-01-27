package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.DATABASE
import i.am.shiro.amai.model.DetailModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers.io

class DetailViewModel : ViewModel() {

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
        book = DATABASE.bookDao.findById(bookId),
        artistTags = DATABASE.tagDao.findNameByIdAndType(bookId, "artist"),
        groupTags = DATABASE.tagDao.findNameByIdAndType(bookId, "group"),
        parodyTags = DATABASE.tagDao.findNameByIdAndType(bookId, "parody"),
        characterTags = DATABASE.tagDao.findNameByIdAndType(bookId, "character"),
        languageTags = DATABASE.tagDao.findNameByIdAndType(bookId, "language"),
        categoryTags = DATABASE.tagDao.findNameByIdAndType(bookId, "category"),
        generalTags = DATABASE.tagDao.findNameByIdAndType(bookId, "tag"),
        pageImages = DATABASE.thumbnailDao.findByBookId(bookId)
    )

    override fun onCleared() {
        disposable.dispose()
    }
}