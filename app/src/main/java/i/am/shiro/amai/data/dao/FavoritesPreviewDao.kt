package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.view.FavoritesPreviewView
import io.reactivex.rxjava3.core.Observable

@Dao
abstract class FavoritesPreviewDao {

    fun findSorted(searchPattern: String, sort: FavoritesSort) = when (sort) {
        FavoritesSort.Old -> findOldest(searchPattern)
        FavoritesSort.New -> findNewest(searchPattern)
    }

    @Query("SELECT * FROM FavoritesPreviewView WHERE title LIKE :searchPattern ORDER BY favoriteDate")
    protected abstract fun findOldest(searchPattern: String): Observable<List<FavoritesPreviewView>>

    @Query("SELECT * FROM FavoritesPreviewView WHERE title LIKE :searchPattern ORDER BY favoriteDate DESC")
    protected abstract fun findNewest(searchPattern: String): Observable<List<FavoritesPreviewView>>
}