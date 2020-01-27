package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.SavedSort
import i.am.shiro.amai.data.view.SavedPreviewView
import io.reactivex.Observable

@Dao
abstract class SavedPreviewDao {

    fun findSorted(searchPattern: String, sort: SavedSort) = when (sort) {
        SavedSort.Old -> findOldest(searchPattern)
        SavedSort.New -> findNewest(searchPattern)
    }

    @Query("SELECT * FROM SavedPreviewView WHERE title LIKE :searchPattern ORDER BY saveDate")
    protected abstract fun findOldest(searchPattern: String): Observable<List<SavedPreviewView>>

    @Query("SELECT * FROM SavedPreviewView WHERE title LIKE :searchPattern ORDER BY saveDate DESC")
    protected abstract fun findNewest(searchPattern: String): Observable<List<SavedPreviewView>>
}