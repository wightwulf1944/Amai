package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.DownloadJobEntity
import io.reactivex.Completable

@Dao
interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(job: DownloadJobEntity): Completable

    @Query("DELETE FROM DownloadJobEntity WHERE bookId = :bookId")
    fun delete(bookId: Int): Completable

    @Query("UPDATE DownloadJobEntity SET isPaused = :isPaused WHERE bookId = :bookId")
    fun setPaused(bookId: Int, isPaused: Boolean): Completable

    @Query("UPDATE DownloadJobEntity SET isPaused = :isPaused")
    fun setAllPaused(isPaused: Boolean): Completable

    @Query("UPDATE DownloadJobEntity SET errorCount = 0 WHERE bookId = :bookId")
    fun resetErrorCount(bookId: Int): Completable

    @Query("SELECT * FROM DownloadJobEntity WHERE isPaused = 0 AND isDone = 0 AND errorCount < 3 ORDER BY createTime")
    fun findAvailableJob(): DownloadJobEntity?

    @Query("UPDATE DownloadJobEntity SET isDownloading = :isDownloading WHERE bookId = :bookId")
    fun setIsDownloading(bookId: Int, isDownloading: Boolean)

    @Query("UPDATE DownloadJobEntity SET isDone = :isDone WHERE bookId = :bookId")
    fun setIsDone(bookId: Int, isDone: Boolean)

    @Query("UPDATE DownloadJobEntity SET errorCount = errorCount + 1 WHERE bookId = :bookId")
    fun incrementErrorCount(bookId: Int)

    @Query("UPDATE DownloadJobEntity SET progressIndex = progressIndex + 1 WHERE bookId = :bookId")
    fun incrementProgress(bookId: Int)
}
