package i.am.shiro.amai.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import i.am.shiro.amai.data.local.intermediate.CachedPreviewIntermediate
import i.am.shiro.amai.data.local.intermediate.DetailIntermediate
import i.am.shiro.amai.data.local.intermediate.FavoritesPreviewIntermediate
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface IntermediateDao {

    @Transaction
    @Query("SELECT * FROM BookEntity WHERE bookId=:bookId")
    fun getDetail(bookId: Int): Flow<DetailIntermediate?>

    @Transaction
    @Query("SELECT * FROM GalleryCacheEntryEntity WHERE cacheId=:cacheId ORDER BY id")
    fun getCachedPreviews(cacheId: Uuid): Flow<List<CachedPreviewIntermediate>>

    @Transaction
    @Query("SELECT * FROM GalleryCacheEntryEntity WHERE cacheId=:cacheId ORDER BY id")
    fun getCachedPreviewsPaging(cacheId: Uuid): PagingSource<Int, CachedPreviewIntermediate>

    @Transaction
    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorites(): Flow<List<FavoritesPreviewIntermediate>>
}
