package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.data.view.ThumbnailView

@Dao
interface ThumbnailDao {

    @Query("SELECT * FROM ThumbnailView WHERE bookId = :bookId ORDER BY pageIndex")
    fun findByBookId(bookId: Int): List<ThumbnailView>
}