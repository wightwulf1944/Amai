package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.FavoriteEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.intermediate.DetailIntermediate
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.Thumbnail
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.util.imageEntities
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign
import timber.log.Timber

class DetailViewModel(
    handle: SavedStateHandle,
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private val bookId by handle.argument<Int> { error("bookId is required") }

    private var localDisposable = Disposable.disposed()

    private var remoteDisposable = Disposable.disposed()

    private val toggleDisposable = CompositeDisposable()

    val isLoadingLive = MutableLiveData<Boolean>()

    val modelLive = MutableLiveData<DetailModel>()

    init {
        load()
    }

    override fun onCleared() {
        localDisposable.dispose()
        remoteDisposable.dispose()
        toggleDisposable.dispose()
    }

    fun toggleFavorite() {
        val isFavorite = modelLive.value?.isFavorite ?: return

        val action = if (isFavorite) {
            database.favoriteDao.deleteById(bookId)
        } else {
            database.favoriteDao.insert(FavoriteEntity(bookId))
        }

        toggleDisposable += action.subscribe({}, Timber::e)
    }

    private fun load() {
        localDisposable = database.detailDao.getDetail(bookId)
            .map { it.toDetailModel() }
            .observeOn(mainThread())
            .subscribe(modelLive::setValue, Timber::e)

        remoteDisposable = nhentaiApi.getOne(bookId)
            .doOnSubscribe { isLoadingLive.postValue(true) }
            .doFinally { isLoadingLive.postValue(false) }
            .retry()
            .subscribe(::onRemoteSuccess, Timber::e)
    }

    private fun DetailIntermediate.toDetailModel(): DetailModel {
        val book = bookEntity

        val tagMap = tagEntities.groupBy(TagEntity::type, TagEntity::name)

        val thumbnails = remoteImageEntities.map {
                Thumbnail(
                    width = it.thumbnailWidth,
                    height = it.thumbnailHeight,
                    url = it.thumbnailUrl
                )
            }

        val isFavorite = favoriteEntity != null

        return DetailModel(
            title = book.title,
            pageCount = book.pageCount,
            tags = tagMap,
            thumbnails = thumbnails,
            isFavorite = isFavorite
        )
    }

    private fun onRemoteSuccess(detailedBookJson: GalleryDetailResponse) {
        with(database) {
            runInTransaction {
                bookDao.insert(detailedBookJson.toEntity())
                tagDao.insert(detailedBookJson.tagEntities())
                imageDao.insert(detailedBookJson.imageEntities())
            }
        }
    }
}