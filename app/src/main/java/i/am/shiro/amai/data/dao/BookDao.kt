package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.BookEntity
import io.reactivex.rxjava3.core.Completable

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: BookEntity)

    @Query("""
        DELETE FROM BookEntity 
        WHERE bookId NOT IN (
            SELECT bookId FROM SavedEntity UNION 
            SELECT bookId FROM CachedEntity)
    """)
    fun deleteOrphan(): Completable
}
