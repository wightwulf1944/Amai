package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.LocalImageEntity
import io.reactivex.rxjava3.core.Completable

@Dao
interface LocalImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: LocalImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<LocalImageEntity>)

    @Query("SELECT * FROM LocalImageEntity WHERE bookId = :bookId ORDER BY pageIndex")
    fun findByBookId(bookId: Int): List<LocalImageEntity>

    @Query("DELETE FROM LocalImageEntity WHERE bookId = :bookId")
    fun deleteById(bookId: Int): Completable
}