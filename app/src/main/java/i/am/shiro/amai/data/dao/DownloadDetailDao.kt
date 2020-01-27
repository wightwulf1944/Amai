package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.data.view.DownloadDetailView
import io.reactivex.Observable

@Dao
interface DownloadDetailDao {

    @Query("SELECT * FROM DownloadDetailView")
    fun getAll(): Observable<List<DownloadDetailView>>
}