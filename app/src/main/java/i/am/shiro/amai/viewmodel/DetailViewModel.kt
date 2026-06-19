package i.am.shiro.amai.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
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
import i.am.shiro.amai.util.savedMutableStateFlow
import i.am.shiro.amai.util.tagEntities
import i.am.shiro.amai.util.toEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel(
    handle: SavedStateHandle,
    private val database: AmaiDatabase,
    private val nhentaiApi: Nhentai.Api
) : ViewModel() {

    private val bookId by handle.savedMutableStateFlow(-1)

    val uiState = bookId
        .flatMapLatest { id -> database.detailDao.getDetail(id) }
        .map { it?.toDetailModel() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun load(bookId: Int) {
        this.bookId.value = bookId

        viewModelScope.launch {
            try {
                val detailedBookJson = nhentaiApi.getOne(bookId)
                onRemoteSuccess(detailedBookJson)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun onFavoriteToggle(isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                if (isFavorite) {
                    database.favoriteDao.insert(FavoriteEntity(bookId.value))
                } else {
                    database.favoriteDao.deleteById(bookId.value)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
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

    private suspend fun onRemoteSuccess(detailedBookJson: GalleryDetailResponse) {
        database.withTransaction {
            database.bookDao.insert(detailedBookJson.toEntity())
            database.tagDao.insert(detailedBookJson.tagEntities())
            database.imageDao.insert(detailedBookJson.imageEntities())
        }
    }
}
