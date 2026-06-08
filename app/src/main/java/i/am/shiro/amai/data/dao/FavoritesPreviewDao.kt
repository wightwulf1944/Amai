package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.view.FavoritesPreviewView
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesPreviewDao {

    fun find(query: String, sort: FavoritesSort): Flow<List<FavoritesPreviewView>> {
        val pattern = "%${query.trim()}%"
        return when (sort) {
            FavoritesSort.New -> findNewest(pattern)
            FavoritesSort.Old -> findOldest(pattern)
        }
    }

    @Query("SELECT * FROM FavoritesPreviewView WHERE title LIKE :searchPattern ORDER BY favoriteDate DESC")
    fun findNewest(searchPattern: String): Flow<List<FavoritesPreviewView>>

    @Query("SELECT * FROM FavoritesPreviewView WHERE title LIKE :searchPattern ORDER BY favoriteDate ASC")
    fun findOldest(searchPattern: String): Flow<List<FavoritesPreviewView>>
}
