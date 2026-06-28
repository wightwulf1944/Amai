package i.am.shiro.amai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Query("DELETE FROM FavoriteEntity WHERE bookId = :bookId")
    suspend fun deleteById(bookId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteEntity: FavoriteEntity)
}
