package i.am.shiro.amai.viewmodel

import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.FavoriteEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.intermediate.DetailIntermediate
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.TagModel
import i.am.shiro.amai.model.Thumbnail
import i.am.shiro.amai.network.GalleryDetailResponse
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.util.imageEntities
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

class DetailViewModel(
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private var bookId: Int = -1

    private var remoteDisposable = Disposable.disposed()

    private var toggleDisposable = Disposable.disposed()

    var uiState: Observable<DetailModel> = Observable.empty()

    fun load(bookId: Int) {
        this.bookId = bookId

        remoteDisposable = nhentaiApi.getOne(bookId)
            .retry()
            .subscribe(::onRemoteSuccess, Timber::e)

        uiState = database.detailDao.getDetail(bookId)
            .map { it.toDetailModel() }
    }

    override fun onCleared() {
        remoteDisposable.dispose()
        toggleDisposable.dispose()
    }

    fun onFavoriteToggle(isFavorite: Boolean) {
        toggleDisposable.dispose()

        val action = if (isFavorite) {
            database.favoriteDao.insert(FavoriteEntity(bookId))
        } else {
            database.favoriteDao.deleteById(bookId)
        }

        toggleDisposable = action.subscribe({}, Timber::e)
    }

    private fun DetailIntermediate.toDetailModel(): DetailModel {
        val book = bookEntity

        val isFavorite = favoriteEntity != null

        val tagMap = tagEntities.groupBy(TagEntity::type) {
            TagModel(it.type, it.name)
        }

        val thumbnails = remoteImageEntities.map {
            val width = it.thumbnailWidth.coerceAtLeast(1).toFloat()
            val height = it.thumbnailHeight.coerceAtLeast(1).toFloat()
            Thumbnail(
                aspectRatio = width / height,
                url = it.thumbnailUrl
            )
        }

        return DetailModel(
            title = book.title,
            pageCount = book.pageCount,
            isFavorite = isFavorite,
            artistTags = tagMap["artist"],
            groupTags = tagMap["group"],
            parodyTags = tagMap["parody"],
            characterTags = tagMap["character"],
            languageTags = tagMap["language"],
            categoryTags = tagMap["category"],
            generalTags = tagMap["tag"],
            thumbnails = thumbnails
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