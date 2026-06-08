package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import i.am.shiro.amai.data.intermediate.DetailIntermediate
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao {

    @Transaction
    @Query("SELECT * FROM BookEntity WHERE bookId=:bookId")
    fun getDetail(bookId: Int): Flow<DetailIntermediate?>
}
