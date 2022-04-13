package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.data.view.CachedPreviewView
import io.reactivex.rxjava3.core.Observable

@Dao
interface CachedPreviewDao {

    @Query("SELECT * FROM CachedPreviewView")
    fun getAll(): Observable<List<CachedPreviewView>>
}