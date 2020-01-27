package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.RemoteImageEntity

@Dao
interface RemoteImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<RemoteImageEntity>)

    @Query("SELECT * FROM RemoteImageEntity WHERE bookId = :bookId AND pageIndex = :pageIndex ORDER BY pageIndex")
    fun findByBookIdAndPageIndex(bookId: Int, pageIndex: Int): RemoteImageEntity
}