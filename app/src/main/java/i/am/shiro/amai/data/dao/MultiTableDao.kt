package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.data.model.DownloadDetail
import io.reactivex.Observable

@Dao
interface MultiTableDao {

    @Query("""
        SELECT
            bookId,
            title,
            pageCount,
            errorCount,
            isPaused,
            COUNT(*) AS localImageCount
        FROM DownloadJobEntity
        LEFT JOIN BookEntity USING(bookId)
        LEFT JOIN (SELECT * FROM LocalImageEntity) USING(bookId)
        GROUP BY bookId
        ORDER BY createTime DESC
    """)
    fun getDownloadDetails(): Observable<List<DownloadDetail>>
}