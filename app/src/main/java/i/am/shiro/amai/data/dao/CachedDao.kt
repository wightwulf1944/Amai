package i.am.shiro.amai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import i.am.shiro.amai.data.entity.CachedEntity

@Dao
interface CachedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CachedEntity)

    @Query("DELETE FROM CachedEntity")
    suspend fun deleteAll()
}
