package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.BookEntity

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookEntity)

    @Query("""
        DELETE FROM BookEntity 
        WHERE bookId NOT IN (
            SELECT bookId FROM FavoriteEntity UNION 
            SELECT bookId FROM CachedEntity)
    """)
    suspend fun deleteOrphan()
}
