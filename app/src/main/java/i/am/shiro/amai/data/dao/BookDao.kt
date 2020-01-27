package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.BookEntity
import io.reactivex.Completable

@Dao
interface BookDao {

    @Query("SELECT * FROM BookEntity WHERE bookId=:id")
    fun findById(id: Int): BookEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookEntity: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookEntities: List<BookEntity>)

    @Query("""
        DELETE FROM BookEntity 
        WHERE bookId NOT IN (SELECT bookId FROM SavedEntity UNION SELECT bookId FROM CachedEntity)
    """)
    fun deleteOrphan(): Completable
}
