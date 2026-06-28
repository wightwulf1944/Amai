package i.am.shiro.amai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.local.entity.ImageEntity

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<ImageEntity>)

    @Query("SELECT * FROM ImageEntity WHERE bookId = :bookId ORDER BY pageIndex")
    suspend fun findByBookId(bookId: Int): List<ImageEntity>
}
