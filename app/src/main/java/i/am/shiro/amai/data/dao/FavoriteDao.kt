package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.FavoriteEntity
import io.reactivex.rxjava3.core.Completable

@Dao
interface FavoriteDao {

    @Query("DELETE FROM FavoriteEntity WHERE bookId = :bookId")
    fun deleteById(bookId: Int): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteEntity: FavoriteEntity): Completable
}