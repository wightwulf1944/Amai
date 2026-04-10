package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.Thumbnail
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.util.imageEntities
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

class DetailViewModel(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private var localDisposable = Disposable.disposed()

    private var remoteDisposable = Disposable.disposed()

    val isLoadingLive = MutableLiveData<Boolean>()

    val modelLive = MutableLiveData<DetailModel>()

    override fun onCleared() {
        localDisposable.dispose()
        remoteDisposable.dispose()
    }

    fun load(bookId: Int) {
        localDisposable = database.detailDao.getDetail(bookId)
            .map { detailIntermediate ->
                val book = detailIntermediate.bookEntity

                val tagMap = detailIntermediate.tagEntities
                    .groupBy(TagEntity::type, TagEntity::name)

                val thumbnails = detailIntermediate.remoteImageEntities
                    .map {
                        Thumbnail(
                            width = it.thumbnailWidth,
                            height = it.thumbnailHeight,
                            url = it.thumbnailUrl
                        )
                    }

                DetailModel(
                    title = book.title,
                    pageCount = book.pageCount,
                    tags = tagMap,
                    thumbnails = thumbnails
                )
            }
            .observeOn(mainThread())
            .subscribe(modelLive::setValue, Timber::e)

        remoteDisposable = nhentaiApi.getOne(bookId)
            .doOnSubscribe { isLoadingLive.postValue(true) }
            .doFinally { isLoadingLive.postValue(false) }
            .retry()
            .subscribe(::onRemoteSuccess, Timber::e)
    }

    private fun onRemoteSuccess(detailedBookJson: GalleryDetailResponse) {
        with(database) {
            runInTransaction {
                bookDao.insert(detailedBookJson.toEntity())
                tagDao.insert(detailedBookJson.tagEntities())
                remoteImageDao.insert(detailedBookJson.imageEntities())
            }
        }
    }
}