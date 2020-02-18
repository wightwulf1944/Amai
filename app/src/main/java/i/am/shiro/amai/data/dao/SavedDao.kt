package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.SavedEntity
import io.reactivex.Completable

@Dao
interface SavedDao {

    @Query("DELETE FROM SavedEntity WHERE bookId = :bookId")
    fun deleteById(bookId: Int): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(savedEntity: SavedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(savedEntities: List<SavedEntity>)
}