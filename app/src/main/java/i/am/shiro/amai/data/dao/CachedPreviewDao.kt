package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.NhentaiSort
import i.am.shiro.amai.data.view.CachedPreviewView
import io.reactivex.Observable

@Dao
abstract class CachedPreviewDao {

    fun getAllSorted(sort: NhentaiSort) = when (sort) {
        NhentaiSort.Popular -> getPopular()
        NhentaiSort.New -> getNewest()
    }

    @Query("SELECT * FROM CachedPreviewView ORDER BY favCount DESC")
    protected abstract fun getPopular(): Observable<List<CachedPreviewView>>

    @Query("SELECT * FROM CachedPreviewView ORDER BY uploadDate DESC")
    protected abstract fun getNewest(): Observable<List<CachedPreviewView>>
}