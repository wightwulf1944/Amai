package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.data.view.PageView

@Dao
interface PageDao {

    @Query("SELECT * FROM PageView WHERE bookId = :bookId ORDER BY pageIndex")
    fun findByBookId(bookId: Int): List<PageView>
}