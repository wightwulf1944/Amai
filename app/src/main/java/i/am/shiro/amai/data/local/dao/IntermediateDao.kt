package i.am.shiro.amai.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import i.am.shiro.amai.data.local.intermediate.CachedPreviewIntermediate
import i.am.shiro.amai.data.local.intermediate.DetailIntermediate
import i.am.shiro.amai.data.local.intermediate.FavoritesPreviewIntermediate
import kotlinx.coroutines.flow.Flow

@Dao
interface IntermediateDao {

    @Transaction
    @Query("SELECT * FROM BookEntity WHERE bookId=:bookId")
    fun getDetail(bookId: Int): Flow<DetailIntermediate?>

    @Transaction
    @Query("SELECT * FROM CachedEntity WHERE cacheKey=:cacheKey ORDER BY id")
    fun getCachedPreviews(cacheKey: String): Flow<List<CachedPreviewIntermediate>>

    @Transaction
    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorites(): Flow<List<FavoritesPreviewIntermediate>>
}
