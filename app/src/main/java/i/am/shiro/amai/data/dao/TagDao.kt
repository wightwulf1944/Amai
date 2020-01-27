package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.TagEntity

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tagEntities: List<TagEntity>)

    @Query("SELECT name FROM TagEntity WHERE bookId=:bookId AND type=:type")
    fun findNameByIdAndType(bookId: Int, type: String): List<String>
}
