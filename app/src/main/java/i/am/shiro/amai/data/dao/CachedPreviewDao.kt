package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Query
import i.am.shiro.amai.data.view.CachedPreviewView
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedPreviewDao {

    @Query("SELECT * FROM CachedPreviewView")
    fun getAll(): Flow<List<CachedPreviewView>>
}
