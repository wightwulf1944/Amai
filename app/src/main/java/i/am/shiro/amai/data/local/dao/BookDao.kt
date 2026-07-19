package i.am.shiro.amai.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import i.am.shiro.amai.data.local.entity.BookEntity

@Dao
interface BookDao {

    @Upsert
    suspend fun insert(entity: BookEntity)

    @Query("""
        DELETE FROM BookEntity 
        WHERE bookId NOT IN (
            SELECT bookId FROM FavoriteEntity UNION 
            SELECT bookId FROM GalleryCacheEntryEntity)
    """)
    suspend fun deleteOrphan()
}
